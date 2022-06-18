package com.meghdut.instagram.downloader.repository

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.apps.inslibrary.entity.userinfo.GraphqlUser
import com.apps.inslibrary.http.InsHttpManager
import com.apps.inslibrary.interfaces.HttpListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserViewModel(val app: Application) : AndroidViewModel(app) {

    val graphqlUserLiveData = MutableLiveData<GraphqlUser>()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    fun loadUser(userName: String) = executor.submit {

        InsHttpManager.queryUserInfoData(userName, object : HttpListener<GraphqlUser> {
            override fun onError(error: String?) {
                throw Exception(error)
            }

            override fun onLogin(isLoggedIn: Boolean) = Unit
            override fun onSuccess(result: GraphqlUser) {
                graphqlUserLiveData.postValue(result)
            }
        })

    }
}