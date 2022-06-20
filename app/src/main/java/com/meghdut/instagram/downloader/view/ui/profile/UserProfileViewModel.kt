package com.meghdut.instagram.downloader.view.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apps.inslibrary.entity.Count
import com.apps.inslibrary.entity.userinfo.EdgeOwnerToTimelineMedia
import com.apps.inslibrary.entity.userinfo.EdgeOwnerToTimelineMediaEdges
import com.apps.inslibrary.entity.userinfo.GraphqlUser
import com.apps.inslibrary.entity.userinfo.PageInfo
import com.apps.inslibrary.http.InsHttpManager
import com.meghdut.instagram.downloader.view.ui.igRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(val app: Application) : AndroidViewModel(app) {

    val graphqlUserLiveData = MutableLiveData<GraphqlUser>()
    private var pageInfo: PageInfo? = null
    private var first = 0
    val edgesFlow = MutableSharedFlow<List<EdgeOwnerToTimelineMediaEdges>>()
    var userName: String = ""

    fun loadUser(userName: String) = viewModelScope.launch {

        if (this@UserProfileViewModel.userName == userName) return@launch

        // send an empty user, to clear the previous data
            graphqlUserLiveData.postValue(GraphqlUser().apply {
                edge_owner_to_timeline_media = EdgeOwnerToTimelineMedia().apply {
                    edges = listOf()
                    count = 0
                    edge_followed_by = Count()
                    edge_follow = Count()
                    pageInfo = null
                }
            })

        igRequest<GraphqlUser> {
            InsHttpManager.queryUserInfoData(userName, it)
        }.collect { result ->
            pageInfo = result.edge_owner_to_timeline_media.pageInfo
            first = result.edge_owner_to_timeline_media.edges.size
            graphqlUserLiveData.postValue(result)
            this@UserProfileViewModel.userName = userName
        }

    }

    fun loadMorePosts() = viewModelScope.launch {
        val info = pageInfo ?: return@launch
        val user = graphqlUserLiveData.value ?: return@launch
        if (canLoadMore) {
            igRequest<GraphqlUser> {
                InsHttpManager.queryUserInfoDataPage(
                    user.id,
                    first,
                    info.end_cursor,
                    it
                )
            }.collect { result ->
                pageInfo = result.edge_owner_to_timeline_media.pageInfo
                first += result.edge_owner_to_timeline_media.edges.size + 1
                viewModelScope.launch {
                    edgesFlow.emit(result.edge_owner_to_timeline_media.edges)
                }
            }
        }
    }

    val canLoadMore: Boolean
        get() = pageInfo?.isHas_next_page == true
}