package com.meghdut.instagram.downloader.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.apps.inslibrary.entity.InstagramUser
import com.apps.inslibrary.entity.userinfo.EdgeOwnerToTimelineMediaEdges
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.databinding.ItemUserListLayoutBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class UserPostItems(val post: EdgeOwnerToTimelineMediaEdges) :
    AbstractBindingItem<ItemUserListLayoutBinding>() {

    var onClickListener: (InstagramUser) -> Unit = {}

    override val type: Int
        get() = R.id.post_item

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemUserListLayoutBinding {
        return ItemUserListLayoutBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemUserListLayoutBinding, payloads: List<Any>) {
        val node = post.node
        binding.ivImg.load(node.thumbnail_src){
            crossfade(true)
        }
        if (node.isIs_video) {
            binding.ivVideo.load(R.drawable.ic_group_video)
        } else if (node.edge_sidecar_to_children?.edges?.isNotEmpty() == true) {
            binding.ivVideo.load(R.drawable.ic_group_imgs)
        } else {
            binding.ivVideo.visibility = View.INVISIBLE
        }

    }
}