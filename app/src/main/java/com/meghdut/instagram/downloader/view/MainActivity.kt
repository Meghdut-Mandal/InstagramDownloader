package com.meghdut.instagram.downloader.view

import android.Manifest
import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.apps.inslibrary.LoginHelper
import com.apps.inslibrary.entity.InstagramData
import com.apps.inslibrary.http.InsHttpManager
import com.apps.inslibrary.interfaces.HttpListener
import com.apps.inslibrary.utils.InsShared
import com.google.android.material.snackbar.Snackbar
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.databinding.ActivityMainBinding
import com.meghdut.instagram.downloader.repository.MainViewModel
import com.meghdut.instagram.downloader.util.DownHistoryHelper
import com.meghdut.instagram.downloader.view.adapters.StoryItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity() {

    private var resSize: Int = 0
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

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            resultLauncher.launch(intent)
        }
        binding.startButton.setOnClickListener {
            downloadReel()
        }
        binding.clearCookies.setOnClickListener {
            LoginHelper.outLogin()
            finish()
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
        viewModel.messageLiveData.observe(this){
            it?.let {
                toast(it)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            findNavController(com.meghdut.instagram.downloader.R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun toast(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show();
    }


    fun loadInsData(str: String) {
        if (!str.contains("instagram.com")) {
            toast("Not a valid link1")
        } else if (DownHistoryHelper.isUrlDownHistory(str)) {
            toast("Already downloaded ")
            viewModel.shareUrl = ""
        } else {
            this.binding.instaLinkEt.setText(str)
//            this.stateDialog.show()
//            this.stateDialog.setWaitDownload()
            if (str.contains("/stories/")) {
                val storiesId = InsManager.getStoriesId(str)
//                FirebaseHelper.onEvent("storiesUrl", "")
//                queryInsStoriesData(storiesId, str)
            } else if (str.contains("/s/") && str.contains("story_media_id=")) {
//                FirebaseHelper.onEvent("userStoriesUrl", "")
//                queryUserStories(str)
            } else if (str.contains("/p/") || str.contains("/reel/") || str.contains("/tv/")) {
//                FirebaseHelper.onEvent("shareUrl", "")
                queryInsShareData(str)
            } else {
                Log.e("TAG", "LINK:$str")
//                this.stateDialog.dismiss()
//                toast(getString(com.meghdut.instagram.downloader.R.string.valid_ins_link))
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    companion object{
        const val RC_PERMISSION = 23
    }

    @AfterPermissionGranted(RC_PERMISSION)
    fun downloadReel() {
        val perms =
            arrayOf(WRITE_EXTERNAL_STORAGE, ACCESS_NETWORK_STATE,READ_EXTERNAL_STORAGE,FOREGROUND_SERVICE,WAKE_LOCK)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            val link = binding.instaLinkEt.text.toString().trim()
            queryInsShareData(link)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Please Approve",
                RC_PERMISSION, *perms);
        }

    }


    private fun queryInsShareData(str: String) {
        val cookies = LoginHelper.getCookies()
        if (TextUtils.isEmpty(cookies)) {
//            queryNoLoginShareData(str)
            return
        }
        val hostUrl = InsManager.getHostUrl(str)
        InsHttpManager.getShareData(hostUrl, cookies, object : HttpListener<InstagramData?> {
            // from class: com.apps.instagram.downloader.fragment.HomeFragment.9
            // com.apps.inslibrary.interfaces.HttpListener
            override fun onLogin(z: Boolean) {}
            override fun onSuccess(instagramData: InstagramData?) {
//                FirebaseHelper.onEvent("verifyShareUrlYes", "")
                instagramData?.shareUrl = str
                instagramData?.viewUrl = str
//                this@HomeFragment.stateDialog.setDownloading()
                viewModel.down(instagramData!!)
            }

            // com.apps.inslibrary.interfaces.HttpListener
            override fun onError(str2: String) {
                Log.e("TAG_1", "onError:$str2")
//                FirebaseHelper.onEvent("verifyShareUrlNo", "")
                toast("Download Failed $str")
            }

        })
    }


}