package com.meghdut.instagram.downloader.view.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.apps.inslibrary.InsManager
import com.apps.inslibrary.LoginHelper
import com.apps.inslibrary.entity.InstagramData
import com.apps.inslibrary.http.InsHttpManager
import com.apps.inslibrary.interfaces.HttpListener
import com.google.android.material.snackbar.Snackbar
import com.meghdut.instagram.downloader.databinding.ActivityMainBinding
import com.meghdut.instagram.downloader.util.DownHistoryHelper
import com.meghdut.instagram.downloader.view.INSTAGRAM_USER
import com.meghdut.instagram.downloader.view.InstagramUserActivity
import com.meghdut.instagram.downloader.view.LoginActivity
import com.meghdut.instagram.downloader.view.adapters.StoriesItems
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var resSize: Int = 0
    private lateinit var binding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModels()
    private val storyItemAdapter by lazy { ItemAdapter<StoriesItems>() }
    private val storiesAdapter by lazy { FastAdapter.with(storyItemAdapter) }


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.loadUserData()
//                println("DONE ${intent.getStringExtra("userid")}")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val root: View = binding.root
        initUI()

        return root
    }

    private fun initUI() {

        lifecycleScope.launchWhenCreated {
            viewModel.loadUserData()
        }


        binding.apply {
            loginButton.setOnClickListener {
                val intent = Intent(context, LoginActivity::class.java)
                resultLauncher.launch(intent)
            }

            startButton.setOnClickListener {
                downloadReel()
            }


            clearCookies.setOnClickListener {
                LoginHelper.outLogin()
                requireActivity().finish()
            }

            val linearLayoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            storiesRv.layoutManager = linearLayoutManager
            storiesRv.adapter = storiesAdapter



            viewModel.apply {
                userInfo.observe(viewLifecycleOwner) {
                    it?.let { reelUser ->
                        userDp.load(reelUser.profile_pic_url)
                        userName.text = reelUser.username
                    }
                }
                userStories.observe(viewLifecycleOwner) { list ->
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
                messageLiveData.observe(viewLifecycleOwner) {
                    it?.let {
                        toast(it)
                    }
                }
            }
        }
    }


    fun toast(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
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
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override fun onPermissionsGranted(requestCode: Int, perms: List<String?>) {
        Log.d("TAG", "onPermissionsGranted:" + requestCode + ":" + perms.size)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String?>) {
        Log.d("TAG", "onPermissionsDenied:" + requestCode + ":" + perms.size)
    }

    companion object {
        const val RC_PERMISSION = 23
    }

    @AfterPermissionGranted(RC_PERMISSION)
    fun downloadReel() {
        val perms =
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.WAKE_LOCK
            )
        if (EasyPermissions.hasPermissions(requireContext(), *perms)) {
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


    private fun launchUserActivity(instagramUser: String) {
        val intent = Intent(activity, InstagramUserActivity::class.java)
        intent.putExtra(INSTAGRAM_USER, instagramUser)
        startActivity(intent)
    }

}