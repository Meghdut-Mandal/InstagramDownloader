package com.meghdut.instagram.downloader.view.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apps.inslibrary.LoginHelper
import com.apps.inslibrary.entity.FollowResult
import com.apps.inslibrary.entity.InstagramUser
import com.apps.inslibrary.http.InsHttpManager
import com.apps.inslibrary.interfaces.HttpListener
import com.meghdut.instagram.downloader.view.ui.igRequest
import kotlinx.coroutines.launch

class AccountsViewModel(application: Application) : AndroidViewModel(application) {

    val recentUsers = MutableLiveData<List<InstagramUser>>()

    fun loadRecentlyVisitedUsers() = viewModelScope.launch {
        val userId = LoginHelper.getUserID()
        val cookie = LoginHelper.getCookies()
        igRequest {
            InsHttpManager.getUserFollows(userId, cookie, it)
        }.collect { result ->
            result?.apply {
                recentUsers.postValue(users)
            }
        }
    }
}