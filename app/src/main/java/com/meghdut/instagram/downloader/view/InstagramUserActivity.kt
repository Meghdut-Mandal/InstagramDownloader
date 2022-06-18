package com.meghdut.instagram.downloader.view

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.apps.inslibrary.entity.InstagramUser
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.databinding.ActivityInstagramUserBinding


class InstagramUserActivity : AppCompatActivity(R.layout.activity_instagram_user) {

    override fun onResume() {
        val rootView = window.decorView.rootView
        val binding = ActivityInstagramUserBinding.bind(rootView)
        init(binding)
    }

    private fun init(binding: ActivityInstagramUserBinding) = binding.apply {
        val user = intent.getSerializableExtra("instagramUser") as? InstagramUser
        if (user != null) {


        }

    }

}