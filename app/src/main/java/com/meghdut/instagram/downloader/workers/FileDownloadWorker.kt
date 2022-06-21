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
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.entity.DownloadItem
import com.meghdut.instagram.downloader.entity.DownloadRequest
import com.meghdut.instagram.downloader.util.ProgressResponseBody
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.Executors
import kotlin.random.Random


class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private var notificationId: Int = -1
    private var downloadedCount = 1
    private lateinit var downloadRequest: DownloadRequest
    private val singleThreadContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    override suspend fun doWork(): Result {

        val requestStr =
            inputData.getString(FileParams.KEY_DOWNLOAD_REQUEST) ?: return Result.failure(
                Data.Builder().put("error", "Download Request was null").build()
            )

        downloadRequest = Gson().fromJson(requestStr, DownloadRequest::class.java)

        Log.d("TAG", "doWork: $requestStr ")

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
        updateNotification(0)

        return withContext(singleThreadContext) {
            val (uri, mimeType) = downloadRequest.downloadItems.map {
                synDownload(it)
            }.firstOrNull() ?: return@withContext Result.failure()

            downloadComplete(uri!!, mimeType)
            Result.success(workDataOf("URI" to uri.toString()))
        }
    }


    private suspend fun synDownload(downloadItem: DownloadItem): Pair<Uri?, String> {

        println("Starting Download for $downloadItem")
        val mimeType = getMimeType(downloadItem.downloadUrl)

        val uri = getSavedFileUri(
            fileName = downloadItem.fileName,
            fileUrl = downloadItem.downloadUrl,
            context = context,
            ::downloadContent
        )
        downloadedCount++
        return uri to mimeType
    }


    private fun downloadComplete(mediaUri: Uri, mimeType: String) {
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
            setOnlyAlertOnce(true)
            setSmallIcon(R.drawable.ic_app)
            setContentTitle("Downloaded ${downloadRequest.userName} content")
            setSubText(downloadRequest.description)
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
            setOnlyAlertOnce(true)
            setSubText(downloadRequest.description)
            setSmallIcon(R.drawable.ic_app)
            if (downloadRequest.downloadItems.size == 1) {
                setContentTitle("Downloading .. ")
            } else {
                setContentTitle("Downloading $downloadedCount of ${downloadRequest.downloadItems.size} ")
            }
            when {
                progress <= 0 -> setProgress(0, 0, true)
                progress in 1..99 -> setProgress(100, progress, false)
            }
        }
        NotificationManagerCompat.from(context)
            .notify(notificationId, builder.build())
    }


    private fun downloadContent(fileUrl: String,outputStream: OutputStream) {
        Thread.sleep(1000)
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

    private suspend fun getSavedFileUri(
        fileName: String,
        fileUrl: String,
        context: Context,
        consumer: (String, OutputStream) -> Unit
    ): Uri? = coroutineScope {
        val mimeType = getMimeType(fileUrl)

        if (mimeType.isEmpty()) return@coroutineScope null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download")
            }

            val resolver = context.contentResolver

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: return@coroutineScope null

            runCatching {
                resolver.openOutputStream(uri).use { output ->
                    consumer(fileUrl, output!!)
                }
            }
            return@coroutineScope uri
        } else {

            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )
            runCatching {
                FileOutputStream(target).use { output ->
                    consumer(fileUrl, output)
                }
            }
            return@coroutineScope target.toUri()
        }
    }

}

object FileParams {
    const val KEY_DOWNLOAD_REQUEST = "key_download_request"
}

object NotificationConstants {
    const val CHANNEL_NAME = "File Download notifications"
    const val CHANNEL_DESCRIPTION =
        "This channel is being used to send notifications related to file downloads"
    const val CHANNEL_ID = "download_file_worker_demo_channel_123456"
}