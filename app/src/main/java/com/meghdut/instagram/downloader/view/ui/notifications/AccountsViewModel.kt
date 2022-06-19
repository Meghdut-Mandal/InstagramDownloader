package com.meghdut.instagram.downloader.view.ui.notifications

import android.app.Application
import androidx.lifecycle.*
import com.apps.inslibrary.LoginHelper
import com.apps.inslibrary.entity.FollowResult
import com.apps.inslibrary.entity.InstagramUser
import com.apps.inslibrary.http.InsHttpManager
import com.apps.inslibrary.interfaces.HttpListener
import kotlinx.coroutines.launch

class AccountsViewModel(application: Application) : AndroidViewModel(application) {

    val recentUsers = MutableLiveData<List<InstagramUser>>()


    fun loadRecentlyVisitedUsers() {
        val userId = LoginHelper.getUserID()
        val cookie = LoginHelper.getCookies()
        viewModelScope.launch {
            InsHttpManager.getUserFollows(userId, cookie, object : HttpListener<FollowResult> {
                override fun onError(error: String?) {
                    throw Exception(error)
                }

                override fun onLogin(isLoggedIn: Boolean) = Unit

                override fun onSuccess(result: FollowResult?) {
                    result?.apply {
                        recentUsers.postValue(users)
                    }
                }
            })
        }
    }
}