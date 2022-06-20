package com.meghdut.instagram.downloader.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.apps.inslibrary.reelentity.ReelsEntity
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.databinding.ItemLatestHeadLayoutBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class StoriesItems(val reelsEntity: ReelsEntity) :
    AbstractBindingItem<ItemLatestHeadLayoutBinding>() {

    var onClickListener: (ReelsEntity) -> Unit = {}

    override val type: Int
        get() = R.id.story_item

    override fun bindView(binding: ItemLatestHeadLayoutBinding, payloads: List<Any>) {
        binding.ivHead.load(reelsEntity.userHead)
        binding.tvAdd.text = reelsEntity.userName
        binding.root.setOnClickListener {
            onClickListener(reelsEntity)
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemLatestHeadLayoutBinding {
        return ItemLatestHeadLayoutBinding.inflate(inflater, parent, false)
    }
}