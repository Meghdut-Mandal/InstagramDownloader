package com.meghdut.instagram.downloader.repository

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.apps.inslibrary.LoginHelper
import com.apps.inslibrary.entity.login.ReelUser
import com.apps.inslibrary.http.HttpManager
import com.apps.inslibrary.http.InsHttpManager
import com.apps.inslibrary.interfaces.HttpListener
import com.apps.inslibrary.reelentity.ReelsEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    val userInfo =  MutableLiveData<ReelUser>()
    val userStories = MutableLiveData<List<ReelsEntity>>()

    fun loadUserData(){
        if (LoginHelper.getIsLogin()) {
            executor.submit {
                loadUserInfo()
            }
            executor.submit {
                loadStories()
            }
        }
    }

    private fun loadUserInfo() {
        val userId = LoginHelper.getUserID()
        HttpManager.queryUserInfoByUserID(userId, object : HttpListener<ReelUser> {
            override fun onError(str: String?) {
                throw Exception("Get user Info Failed $str")
            }
            override fun onLogin(z: Boolean) {
            }
            override fun onSuccess(user: ReelUser?) {
                LoginHelper.addUserInfo(user)
                userInfo.postValue(user)
            }
        })

    }

    private fun loadStories(){
        val cookies = LoginHelper.getCookies()
        InsHttpManager.getReelsTrayData(cookies,object: HttpListener<List<ReelsEntity>>{
            override fun onError(str: String?) {
                userStories.postValue(listOf())
                throw Exception("Get stories Failed $str")
            }

            override fun onLogin(z: Boolean) {
                userStories.postValue(listOf())
            }

            override fun onSuccess(result: List<ReelsEntity>) {
              userStories.postValue(result)
            }
        })
    }
}