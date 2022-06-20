package com.meghdut.instagram.downloader.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.util.ProgressResponseBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.random.Random


class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private var notificationId: Int = -1
    override suspend fun doWork(): Result {

        val fileUrl = inputData.getString(FileParams.KEY_FILE_URL) ?: ""
        val fileName = inputData.getString(FileParams.KEY_FILE_NAME) ?: ""

        Log.d("TAG", "doWork: $fileUrl | $fileName ")


        if (fileName.isEmpty()
            || fileUrl.isEmpty()
        ) {
            Result.failure()
        }
        notificationId = Random.nextInt(10_0000_000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = NotificationConstants.CHANNEL_NAME
            val description = NotificationConstants.CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NotificationConstants.CHANNEL_ID, name, importance)
            channel.description = description

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)

        }
        val mimeType = getMimeType(fileUrl)

        updateNotification(0)

        val uri = getSavedFileUri(
            fileName = fileName,
            fileUrl = fileUrl,
            context = context,
            ::downloadContent
        )

        return if (uri != null) {
            downloadComplete(uri,mimeType)
            Result.success(workDataOf(FileParams.KEY_FILE_URI to uri.toString()))
        } else {
            Result.failure()
        }

    }

    private fun downloadComplete(mediaUri: Uri,mimeType:String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, mediaUri)
        shareIntent.type = mimeType
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val intentAction = Intent.createChooser(
            shareIntent,
            "share"
        )
        val shareActionPendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.getActivity(
                context,
                1,
                intentAction,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            ) else PendingIntent.getActivity(
                context,
                1,
                intentAction,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID).apply {
            setOngoing(false)
            setSmallIcon(R.drawable.ic_app)
            setContentTitle("Download complete")
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_share,
                    "Share",
                    shareActionPendingIntent
                )
            )
        }
        NotificationManagerCompat.from(context)
            .notify(notificationId, builder.build())
    }

    private fun updateNotification(progress: Int) {
        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID).apply {
            setOngoing(true)
            setSmallIcon(R.drawable.ic_app)
            setContentTitle("Downloading your file...")
            when {
                progress <= 0 -> setProgress(0, 0, true)
                progress in 1..99 -> setProgress(100, progress, false)
            }
        }
        NotificationManagerCompat.from(context)
            .notify(notificationId, builder.build())
    }


    private fun downloadContent(fileUrl: String,outputStream: OutputStream) {
        val progressListener: ProgressResponseBody.ProgressListener =
            object : ProgressResponseBody.ProgressListener {
                var time: Long = 0
                override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                    if (!done) {
                        if (contentLength != -1L) {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - time > 1000) {
                                time = currentTime
                                updateNotification(
                                    (100 * bytesRead / contentLength).toInt()
                                )
                            }
                        }
                    }
                }
            }
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(Interceptor { chain: Interceptor.Chain ->
                val originalResponse = chain.proceed(chain.request())
                originalResponse.newBuilder()
                    .body(
                        ProgressResponseBody(
                            originalResponse.body,
                            progressListener
                        )
                    )
                    .build()
            })
            .build()

        val request = Request.Builder().url(fileUrl).get().build()
        val response = client.newCall(request).execute()
        response.body?.byteStream().use { input->
            input?.copyTo(outputStream)
        }

    }

    private fun getMimeType(url: String): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(url) ?: return ""
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: ""
    }

    private fun getSavedFileUri(
        fileName: String,
        fileUrl: String,
        context: Context,
        consumer: (String,OutputStream) -> Unit
    ): Uri? {
        val mimeType = getMimeType(fileUrl)

        if (mimeType.isEmpty()) return null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download")
            }

            val resolver = context.contentResolver

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            return if (uri != null) {
                resolver.openOutputStream(uri).use { output ->
                    consumer(fileUrl,output!!)
                }
                uri
            } else {
                null
            }

        } else {

            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )
            FileOutputStream(target).use { output ->
                consumer(fileUrl,output)
            }
            return target.toUri()
        }
    }

}

object FileParams {
    const val KEY_FILE_URL = "key_file_url"
    const val KEY_FILE_NAME = "key_file_name"
    const val KEY_FILE_URI = "key_file_uri"
}

object NotificationConstants {
    const val CHANNEL_NAME = "File Download notifications"
    const val CHANNEL_DESCRIPTION =
        "This channel is being used to send notifications related to file downloads"
    const val CHANNEL_ID = "download_file_worker_demo_channel_123456"
}