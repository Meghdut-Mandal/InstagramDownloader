package com.meghdut.instagram.downloader.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.apps.inslibrary.entity.InstagramUser
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.databinding.ActivityInstagramUserBinding
import com.meghdut.instagram.downloader.repository.UserViewModel
import com.meghdut.instagram.downloader.util.format

val INSTAGRAM_USER = "instagramUser"

class InstagramUserActivity : AppCompatActivity(R.layout.activity_instagram_user) {
    private val viewModel: UserViewModel by viewModels()


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
        viewModel.graphqlUserLiveData.observe(this@InstagramUserActivity) {
            it?.let { graphqlUser ->
                userBioTv.text = graphqlUser.biography
                toolbar.title = graphqlUser.full_name
                userProfile.load(graphqlUser.profile_pic_url_hd)
                userPostTv.text = graphqlUser.edge_owner_to_timeline_media.count.format()
                userFollowTv.text = graphqlUser.edge_followed_by.count.format()
                userFollowingTv.text = graphqlUser.edge_follow.count.format()
            }
        }

        val username = intent.getSerializableExtra(INSTAGRAM_USER) as? String
        if (username != null) {
            viewModel.loadUser(username)
            toolbar.subtitle = username

        }

    }

}