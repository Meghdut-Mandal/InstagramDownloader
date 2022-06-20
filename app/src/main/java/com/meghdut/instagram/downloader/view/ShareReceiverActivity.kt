package com.meghdut.instagram.downloader.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apps.inslibrary.InsManager
import com.apps.inslibrary.LoginHelper
import com.meghdut.instagram.downloader.view.ui.home.HomeViewModel

class ShareReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        InsManager.init(application)
        val viewModel = HomeViewModel(application)
        super.onCreate(savedInstanceState)
        val shareUrl = intent.getStringExtra("android.intent.extra.TEXT")?:"None"
        if (shareUrl.startsWith("https://www.instagram.com") && LoginHelper.getIsLogin()){
            viewModel.downloadReel(shareUrl)
        }

        finish()
    }
}