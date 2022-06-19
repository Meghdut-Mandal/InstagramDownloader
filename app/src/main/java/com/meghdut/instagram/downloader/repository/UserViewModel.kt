package com.meghdut.instagram.downloader.repository

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apps.inslibrary.entity.userinfo.EdgeOwnerToTimelineMediaEdges
import com.apps.inslibrary.entity.userinfo.GraphqlUser
import com.apps.inslibrary.entity.userinfo.PageInfo
import com.apps.inslibrary.http.InsHttpManager
import com.apps.inslibrary.interfaces.HttpListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserViewModel(val app: Application) : AndroidViewModel(app) {

    val graphqlUserLiveData = MutableLiveData<GraphqlUser>()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private var pageInfo: PageInfo? = null
    private var first = 0
    val edgesFlow = MutableSharedFlow<List<EdgeOwnerToTimelineMediaEdges>>()

    fun loadUser(userName: String) = executor.submit {

        InsHttpManager.queryUserInfoData(userName, object : HttpListener<GraphqlUser> {
            override fun onError(error: String?) {
                throw Exception(error)
            }

            override fun onLogin(isLoggedIn: Boolean) = Unit
            override fun onSuccess(result: GraphqlUser) {
                pageInfo = result.edge_owner_to_timeline_media.pageInfo
                first = result.edge_owner_to_timeline_media.edges.size
                viewModelScope.launch {
                    graphqlUserLiveData.postValue(result)
                }
            }
        })
    }

    fun loadMorePosts() {
        val info = pageInfo ?: return
        val user = graphqlUserLiveData.value ?: return
        if (canLoadMore) {
            val httpListner = object : HttpListener<GraphqlUser> {
                override fun onError(error: String?) {
                    throw Exception(error)
                }

                override fun onLogin(isLoggedIn: Boolean) = Unit

                override fun onSuccess(result: GraphqlUser?) {
                    if (result == null) return
                    pageInfo = result.edge_owner_to_timeline_media.pageInfo
                    first += result.edge_owner_to_timeline_media.edges.size + 1
                    viewModelScope.launch {
                        edgesFlow.emit(result.edge_owner_to_timeline_media.edges)
                    }
                }
            }

            InsHttpManager.queryUserInfoDataPage(
                user.id,
                first,
                info.end_cursor,
                httpListner
            )
        }
    }

    val canLoadMore: Boolean
        get() = pageInfo?.isHas_next_page == true
}