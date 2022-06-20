package com.meghdut.instagram.downloader.view.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.apps.inslibrary.LoginHelper
import com.google.android.material.snackbar.Snackbar
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.databinding.ActivityMainBinding
import com.meghdut.instagram.downloader.view.LoginActivity
import com.meghdut.instagram.downloader.view.adapters.StoriesItems
import com.meghdut.instagram.downloader.view.ui.profile.UserProfileViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModels({ requireActivity() })
    private val profileViewModel: UserProfileViewModel by viewModels({ requireActivity() })
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

            if (!LoginHelper.getIsLogin()) {
                logInPrompt.visibility = View.VISIBLE
            }
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
                        logInPrompt.visibility = View.INVISIBLE
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
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
            viewModel.downloadReel(link)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, "Please Approve",
                RC_PERMISSION, *perms
            );
        }

    }


    private fun launchUserActivity(instagramUser: String) {
        profileViewModel.loadUser(instagramUser)
        val navController = findNavController()
        navController.navigate(R.id.userProfileFragment)
    }

}