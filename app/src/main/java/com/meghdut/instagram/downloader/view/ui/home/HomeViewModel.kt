package com.meghdut.instagram.downloader.view.ui.home

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.apps.inslibrary.InsManager
import com.apps.inslibrary.InstagramRes
import com.apps.inslibrary.LoginHelper
import com.apps.inslibrary.entity.InstagramData
import com.apps.inslibrary.entity.login.ReelUser
import com.apps.inslibrary.http.HttpManager
import com.apps.inslibrary.http.InsHttpManager
import com.apps.inslibrary.reelentity.ReelsEntity
import com.apps.inslibrary.utils.DownUtils
import com.google.gson.Gson
import com.meghdut.instagram.downloader.entity.DownloadItem
import com.meghdut.instagram.downloader.entity.DownloadRequest
import com.meghdut.instagram.downloader.util.DownHistoryHelper
import com.meghdut.instagram.downloader.view.ui.igRequest
import com.meghdut.instagram.downloader.workers.FileDownloadWorker
import com.meghdut.instagram.downloader.workers.FileParams
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeViewModel(val app: Application) : AndroidViewModel(app) {

    var shareUrl: String = ""
    val userInfo = MutableLiveData<ReelUser>()
    val userStories = MutableLiveData<List<ReelsEntity>>()
    val messageLiveData = MutableLiveData<String>()
    val messageFlow = MutableSharedFlow<String>()
    var userId = ""

    fun loadUserData() {
        if (LoginHelper.getIsLogin() && userId != LoginHelper.getUserID()) {
            loadCurrentUserInfo()
            loadStories()
            userId = LoginHelper.getUserID()
        }
    }

    private fun loadCurrentUserInfo() = viewModelScope.launch {
        val userId = LoginHelper.getUserID()
        igRequest {
            HttpManager.queryUserInfoByUserID(userId, it)
        }.collectLatest { user ->
            LoginHelper.addUserInfo(user)
            userInfo.postValue(user)
        }
    }

    fun post(text: String) {
        messageLiveData.postValue(text)
    }

    private fun loadStories() = viewModelScope.launch {
        val cookies = LoginHelper.getCookies()
        igRequest {
            InsHttpManager.getReelsTrayData(cookies, it)
        }.collectLatest {
            userStories.postValue(it)
        }
    }

    fun down(instagramData: InstagramData) {
        val instagramResources = instagramData.instagramRes
        if (instagramResources != null && instagramResources.size > 0) {
            val downloadRequest =
                DownloadRequest(instagramData.instagramUser.full_name, instagramData.describe)
            val items = instagramResources.map { instagramRes ->
                val downloadUrl =
                    if (instagramRes.isIs_video) instagramRes.video_url else instagramRes.display_url

                val filenameFromURL =
                    if (instagramRes.isIs_video)
                        DownUtils.getFilenameFromURL(downloadUrl)
                    else DownUtils.getImageFilenameFromURL(downloadUrl)

                DownloadItem(filenameFromURL, downloadUrl)
            }
            downloadRequest.downloadItems = items
            startDownload(downloadRequest)
        }
    }


    private fun startDownload(downloadRequest: DownloadRequest) {

        val requestStr = Gson().toJson(downloadRequest)
        val data = Data.Builder()

        data.apply {
            putString(FileParams.KEY_DOWNLOAD_REQUEST, requestStr)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val fileDownloadWorker = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager
            .getInstance(app)
            .enqueueUniqueWork(
                "oneFileDownloadWork_${System.currentTimeMillis()}",
                ExistingWorkPolicy.KEEP,
                fileDownloadWorker
            )
    }

    fun downloadReel(str: String) = viewModelScope.launch {
        val cookies = LoginHelper.getCookies()
        if (TextUtils.isEmpty(cookies)) {
//            queryNoLoginShareData(str)
            return@launch
        }
        val hostUrl = InsManager.getHostUrl(str)
        igRequest {
            InsHttpManager.getShareData(hostUrl, cookies, it)
        }.collectLatest { instagramData ->
            instagramData?.shareUrl = str
            instagramData?.viewUrl = str
            down(instagramData!!)
        }
    }


    fun loadInsData(str: String) = viewModelScope.launch {
        if (!str.contains("instagram.com")) {
            messageFlow.emit("Not a valid link1")
        } else if (DownHistoryHelper.isUrlDownHistory(str)) {
            messageFlow.emit("Already downloaded ")
            shareUrl = ""
        } else {
//            this.binding.instaLinkEt.setText(str)
//            this.stateDialog.show()
//            this.stateDialog.setWaitDownload()
            if (str.contains("/stories/")) {
                val storiesId = InsManager.getStoriesId(str)
//                FirebaseHelper.onEvent("storiesUrl", "")
//                queryInsStoriesData(storiesId, str)
            } else if (str.contains("/s/") && str.contains("story_media_id=")) {
//                FirebaseHelper.onEvent("userStoriesUrl", "")
//                queryUserStories(str)
            } else if (str.contains("/p/") || str.contains("/reel/") || str.contains("/tv/")) {
//                FirebaseHelper.onEvent("shareUrl", "")
                downloadReel(str)
            } else {
                Log.e("TAG", "LINK:$str")
//                this.stateDialog.dismiss()
//                toast(getString(com.meghdut.instagram.downloader.R.string.valid_ins_link))
            }
        }
    }


    private fun queryUserStories(url: String) {
        var cookies = LoginHelper.getCookies()
        if (TextUtils.isEmpty(cookies)) {
            cookies = LoginHelper.getTmpCookies()
            if (TextUtils.isEmpty(cookies)) {
                TODO("not yet done")
//                TODO()
//                queryTmpCookies(true, "", url)
                return
            }
        }
        getRedirectUrl(cookies, url)
    }

    fun getRedirectUrl(cookies: String, url: String) {

//        AnonymousClass12(str2, url).start()
    }


}