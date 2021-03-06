package com.meghdut.instagram.downloader.view.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.databinding.FragmentAccountsBinding
import com.meghdut.instagram.downloader.view.adapters.UserItems
import com.meghdut.instagram.downloader.view.ui.profile.UserProfileViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class AccountsFragment : Fragment() {

    private lateinit var binding: FragmentAccountsBinding
    private val viewModel: AccountsViewModel by viewModels()
    private val userItemAdapter by lazy { ItemAdapter<UserItems>() }
    private val profileViewModel: UserProfileViewModel by viewModels({ requireActivity() })

    private val userAdapter by lazy { FastAdapter.with(userItemAdapter) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountsBinding.inflate(inflater, container, false)
        initUi()
        return binding.root
    }

    private fun initUi() = binding.apply {
        val gridLayoutManager = GridLayoutManager(context,5).apply {
            orientation = GridLayoutManager.VERTICAL
        }
        recentUsers.layoutManager = gridLayoutManager
        recentUsers.adapter = userAdapter

        lifecycleScope.launchWhenCreated {
            viewModel.loadRecentlyVisitedUsers()
        }

        viewModel.recentUsers.observe(viewLifecycleOwner) { list ->
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
    }

    private fun launchUserActivity(instagramUser: String) {
        profileViewModel.loadUser(instagramUser)
        val navController = findNavController()
        navController.navigate(R.id.userProfileFragment)
    }


}