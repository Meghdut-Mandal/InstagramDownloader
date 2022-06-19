package com.meghdut.instagram.downloader.view

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
import com.google.android.material.snackbar.Snackbar
import com.meghdut.instagram.downloader.databinding.ActivityMainBinding
import com.meghdut.instagram.downloader.repository.MainViewModel
import com.meghdut.instagram.downloader.util.DownHistoryHelper
import com.meghdut.instagram.downloader.view.adapters.StoriesItems
import com.meghdut.instagram.downloader.view.adapters.UserItems
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : AppCompatActivity() {

    private var resSize: Int = 0
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()
    private val storyItemAdapter by lazy { ItemAdapter<StoriesItems>() }
    private val userItemAdapter by lazy { ItemAdapter<UserItems>() }

    private val storiesAdapter by lazy { FastAdapter.with(storyItemAdapter) }
    private val userAdapter by lazy { FastAdapter.with(userItemAdapter) }


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


        binding.apply {
            loginButton.setOnClickListener {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                resultLauncher.launch(intent)
            }

            startButton.setOnClickListener {
                downloadReel()
            }


            clearCookies.setOnClickListener {
                LoginHelper.outLogin()
                finish()
            }

//            viewModel.loadUserData()

            val linearLayoutManager = LinearLayoutManager(applicationContext).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            storiesRv.layoutManager = linearLayoutManager
            storiesRv.adapter = storiesAdapter

            val linearLayoutManager2 = LinearLayoutManager(applicationContext).apply {
                orientation = LinearLayoutManager.HORIZONTAL

            }
            recentUsers.layoutManager = linearLayoutManager2
            recentUsers.adapter = userAdapter

            viewModel.apply {
//                loadData.setOnClickListener {
                loadUserData()
//                }

                recentUsers.observe(this@MainActivity) { list ->
                    list?.let { recentUser ->
                        val userList = recentUser.map { it ->
                            UserItems(it).apply {
                                onClickListener = { instagramUser ->
                                    launchUserActivity(instagramUser.username)
                                }
                            }
                        }
                        userItemAdapter.set(userList)
                    }
                }
                userInfo.observe(this@MainActivity) {
                    it?.let { reelUser ->
                        userDp.load(reelUser.profile_pic_url)
                        userName.text = reelUser.username
                    }
                }
                userStories.observe(this@MainActivity) { list ->
                    list?.let { stories ->
                        val storyList = stories.map {
                            StoriesItems(it).apply {
                                onClickListener = {
                                    launchUserActivity(it.userName)
                                }
                            }
                        }
                        storyItemAdapter.set(storyList)
                    }
                }
                messageLiveData.observe(this@MainActivity) {
                    it?.let {
                        toast(it)
                    }
                }
            }
        }

    }

    private fun launchUserActivity(instagramUser: String) {
        val intent = Intent(applicationContext, InstagramUserActivity::class.java)
        intent.putExtra(INSTAGRAM_USER, instagramUser)
        startActivity(intent)
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


    private fun queryUserStories(url: String) {
        var cookies = LoginHelper.getCookies()
        if (TextUtils.isEmpty(cookies)) {
            cookies = LoginHelper.getTmpCookies()
            if (TextUtils.isEmpty(cookies)) {
                TODO("not yet done")
//                TODO()
//                queryTmpCookies(true, "", url)
                return
            }
        }
        getRedirectUrl(cookies, url)
    }

    fun getRedirectUrl(cookies: String, url: String) {

//        AnonymousClass12(str2, url).start()
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

    companion object {
        const val RC_PERMISSION = 23
    }

    @AfterPermissionGranted(RC_PERMISSION)
    fun downloadReel() {
        val perms =
            arrayOf(
                WRITE_EXTERNAL_STORAGE,
                ACCESS_NETWORK_STATE,
                READ_EXTERNAL_STORAGE,
                FOREGROUND_SERVICE,
                WAKE_LOCK
            )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            val link = binding.instaLinkEt.text.toString().trim()
            queryInsShareData(link)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, "Please Approve",
                RC_PERMISSION, *perms
            );
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
            override fun onLogin(z: Boolean) {

            }

            override fun onSuccess(instagramData: InstagramData?) {
                instagramData?.shareUrl = str
                instagramData?.viewUrl = str
                viewModel.down(instagramData!!)
            }

            override fun onError(str2: String) {
                Log.e("TAG_1", "onError:$str2")
                toast("Download Failed $str")
            }

        })
    }


}