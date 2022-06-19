package com.meghdut.instagram.downloader.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.databinding.ActivityInstagramUserBinding
import com.meghdut.instagram.downloader.repository.UserViewModel
import com.meghdut.instagram.downloader.util.format
import com.meghdut.instagram.downloader.view.adapters.UserPostItems
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter.Companion.items
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

val INSTAGRAM_USER = "instagramUser"

class InstagramUserActivity : AppCompatActivity(R.layout.activity_instagram_user) {
    private val viewModel: UserViewModel by viewModels()
    private val userPostItemsAdapter by lazy { ItemAdapter<UserPostItems>() }
    private val footerAdapter: GenericItemAdapter = items()
    private val adapter = FastAdapter.with(listOf(userPostItemsAdapter, footerAdapter))

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityInstagramUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init(binding)
    }


    private fun init(binding: ActivityInstagramUserBinding) = binding.apply {
        val gridLayoutManager = GridLayoutManager(this@InstagramUserActivity, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        postsRecyclerView.addItemDecoration(
            GridItemDecoration.Builder(this@InstagramUserActivity)
                .setHorizontalSpan(5.0f)
                .setVerticalSpan(5.0f)
                .setColor(R.color.white)
                .setShowLastLine(false).build()
        )
        postsRecyclerView.layoutManager = gridLayoutManager
        postsRecyclerView.adapter = adapter
        val endlessRecyclerOnScrollListener =
            object : EndlessRecyclerOnScrollListener(footerAdapter) {
                override fun onLoadMore(currentPage: Int) {
                    if (viewModel.canLoadMore){
                        footerAdapter.clear()
                        val progressItem = ProgressItem()
                        progressItem.isEnabled = false
                        footerAdapter.add(progressItem)
                        viewModel.loadMorePosts()
                    }
                }
            }
        postsRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener)

        lifecycleScope.launchWhenStarted {
            viewModel.edgesFlow.collectLatest { list ->
                userPostItemsAdapter.add(list.map { UserPostItems(it) })
                footerAdapter.clear()
            }
        }

        viewModel.graphqlUserLiveData.observe(this@InstagramUserActivity) {
            it?.let { graphqlUser ->
                userBioTv.text = graphqlUser.biography
                toolbar.title = graphqlUser.full_name
                userProfile.load(graphqlUser.profile_pic_url_hd)
                userPostTv.text = graphqlUser.edge_owner_to_timeline_media.count.format()
                userFollowTv.text = graphqlUser.edge_followed_by.count.format()
                userFollowingTv.text = graphqlUser.edge_follow.count.format()

                val list = graphqlUser.edge_owner_to_timeline_media.edges.map { UserPostItems(it) }
                userPostItemsAdapter.set(list)
            }
        }

        val username = intent.getSerializableExtra(INSTAGRAM_USER) as? String
        if (username != null) {
            viewModel.loadUser(username)
            toolbar.subtitle = username

        }

    }

}