package com.meghdut.instagram.downloader.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.apps.inslibrary.entity.InstagramUser
import com.meghdut.instagram.downloader.databinding.ItemLatestHeadLayoutBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class UserItems(val instagramUser: InstagramUser) :
    AbstractBindingItem<ItemLatestHeadLayoutBinding>() {

    override val type: Int
        get() = 23

    var onClickListener: (InstagramUser) -> Unit = {}


    override fun bindView(binding: ItemLatestHeadLayoutBinding, payloads: List<Any>) {
        binding.ivHead.load(instagramUser.profile_pic_url)
        binding.tvAdd.text = instagramUser.username
        binding.root.setOnClickListener {
            onClickListener(instagramUser)
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemLatestHeadLayoutBinding {
        return ItemLatestHeadLayoutBinding.inflate(inflater, parent, false)
    }
}