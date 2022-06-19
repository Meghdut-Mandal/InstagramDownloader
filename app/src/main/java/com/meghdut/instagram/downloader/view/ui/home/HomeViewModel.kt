package com.meghdut.instagram.downloader.view.ui.home

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apps.inslibrary.InsManager
import com.apps.inslibrary.InstagramRes
import com.apps.inslibrary.LoginHelper
import com.apps.inslibrary.entity.InstagramData
import com.apps.inslibrary.entity.login.ReelUser
import com.apps.inslibrary.http.HttpManager
import com.apps.inslibrary.http.InsHttpManager
import com.apps.inslibrary.interfaces.HttpListener
import com.apps.inslibrary.reelentity.ReelsEntity
import com.apps.inslibrary.utils.DownUtils
import com.apps.inslibrary.utils.DownUtils.OnDownCallback
import com.meghdut.instagram.downloader.util.DownHistoryHelper
import com.meghdut.instagram.downloader.view.ui.igRequest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    var shareUrl: String = ""
    private var resSize: Int = 0
    val userInfo = MutableLiveData<ReelUser>()
    val userStories = MutableLiveData<List<ReelsEntity>>()
    val messageLiveData = MutableLiveData<String>()

    fun loadUserData() {
        if (LoginHelper.getIsLogin()) {
            loadCurrentUserInfo()
            loadStories()
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
        val instagramRes = instagramData.instagramRes
        if (instagramRes != null && instagramRes.size > 0) {
            val size = instagramRes.size
            this.resSize = size
            for (instagramRes2 in instagramRes) {
                startSingleDown(instagramRes2, instagramData)
            }
        }
    }

    private fun startSingleDown(instagramRes: InstagramRes, instagramData: InstagramData) {
        val instagramUser = instagramData.instagramUser
        val video_url =
            if (instagramRes.isIs_video) instagramRes.video_url else instagramRes.display_url
        DownUtils.down(
            video_url,
            DownUtils.getSaveFile(instagramUser, video_url, instagramRes.isIs_video),
            object : OnDownCallback {

                override fun onLoading(j: Long, j2: Long) {}

                override fun onStart() {}

                override fun onSuccess(file: File) {
                    instagramRes.saveFile = file.absolutePath
                    this@HomeViewModel.shareUrl = ""
                    DownHistoryHelper.addDownHistory(instagramData)
                    post("Download Complete !")
                }

                // com.apps.instagram.downloader.utils.DownUtils.OnDownCallback
                override fun onError(str: String) {
                    Log.e("TAG", "：$str")
                    throw Exception(str)
//                    FirebaseHelper.onEvent("downloaderNo", "")
//                    FirebaseHelper.onEvent(instagramData.shareUrl, "")
//                    this@HomeFragment.ll_down.setFocusable(true)
//                    this@HomeFragment.ll_down.setClickable(true)
//                    this@HomeFragment.stateDialog.setTryAgain(instagramData.shareUrl)
                }
            })
    }

    fun queryInsShareData(str: String) = viewModelScope.launch {
        val cookies = LoginHelper.getCookies()
        if (TextUtils.isEmpty(cookies)) {
//            queryNoLoginShareData(str)
            return@launch
        }
        val hostUrl = InsManager.getHostUrl(str)
        igRequest {
            InsHttpManager.getShareData(hostUrl, cookies,it)
        }.collectLatest { instagramData ->
            instagramData?.shareUrl = str
            instagramData?.viewUrl = str
            down(instagramData!!)
        }
    }


    fun loadInsData(str: String) {
        if (!str.contains("instagram.com")) {
//            toast("Not a valid link1")
        } else if (DownHistoryHelper.isUrlDownHistory(str)) {
//            toast("Already downloaded ")
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
                queryInsShareData(str)
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