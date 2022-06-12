package com.meghdut.instagram.downloader.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.apps.inslibrary.InsManager
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.databinding.ActivityMainBinding
import com.meghdut.instagram.downloader.repository.MainViewModel
import com.meghdut.instagram.downloader.view.adapters.StoryItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()
    private val itemAdapter by lazy { ItemAdapter<StoryItem>() }

    private val fastAdapter by lazy { FastAdapter.with(itemAdapter) }


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.loadUserData()
                println("DONE ${intent.getStringExtra("userid")}")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InsManager.init(application)

        binding.button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            resultLauncher.launch(intent)
        }
        viewModel.loadUserData()
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.storiesRv.layoutManager = linearLayoutManager
        binding.storiesRv.adapter = fastAdapter


        viewModel.userInfo.observe(this) {
            it?.let { reelUser ->
                binding.userDp.load(reelUser.profile_pic_url)
                binding.userName.text = reelUser.username
            }
        }
        viewModel.userStories.observe(this) { list ->
            list?.let { stories ->
                val storyList = stories.map { StoryItem(it) }
                itemAdapter.set(storyList)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}