package com.meghdut.instagram.downloader.view.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.meghdut.instagram.downloader.databinding.FragmentDownloadsBinding

class DownloadsFragment : Fragment() {

    val viewModel: DownloadsViewModel by viewModels()
    private lateinit var binding: FragmentDownloadsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadsBinding.inflate(inflater, container, false)
       return binding.root
    }


}