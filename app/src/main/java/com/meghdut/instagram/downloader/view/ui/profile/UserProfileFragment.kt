package com.meghdut.instagram.downloader.view.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.meghdut.instagram.downloader.R
import com.meghdut.instagram.downloader.databinding.FragmentUserProfileBinding
import com.meghdut.instagram.downloader.util.format
import com.meghdut.instagram.downloader.view.GridItemDecoration
import com.meghdut.instagram.downloader.view.adapters.UserPostItems
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.fastadapter.ui.items.ProgressItem
import kotlinx.coroutines.flow.collectLatest

class UserProfileFragment : Fragment() {


    private val viewModel: UserProfileViewModel by viewModels({ requireActivity() })
    private lateinit var binding: FragmentUserProfileBinding


    private val userPostItemsAdapter by lazy { ItemAdapter<UserPostItems>() }
    val progressItem = ProgressItem()
    private val footerAdapter: GenericItemAdapter = ItemAdapter.items<GenericItem>().also {
        it.add(progressItem)
    }
    private val adapter = FastAdapter.with(listOf(userPostItemsAdapter, footerAdapter))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        initUi()

        return binding.root
    }

    private fun initUi() = binding.apply {

        val supportActionBar = (requireActivity() as? AppCompatActivity)?.supportActionBar

        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        postsRecyclerView.addItemDecoration(
            GridItemDecoration.Builder(requireContext())
                .setHorizontalSpan(5.0f)
                .setVerticalSpan(5.0f)
                .setColor(R.color.white)
                .setShowLastLine(false).build()
        )
        postsRecyclerView.layoutManager = gridLayoutManager
        postsRecyclerView.adapter = adapter
        val endlessRecyclerOnScrollListener =
            object : EndlessRecyclerOnScrollListener(footerAdapter) {
                override fun onLoadMore(currentPage: Int) {
                    if (viewModel.canLoadMore){
                        progressItem.isEnabled = true
                        viewModel.loadMorePosts()
                    }
                }
            }
        postsRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener)

        lifecycleScope.launchWhenStarted {
            viewModel.edgesFlow.collectLatest { list ->
                userPostItemsAdapter.add(list.map { UserPostItems(it) })
                progressItem.isEnabled = false
            }
        }

        viewModel.graphqlUserLiveData.observe(viewLifecycleOwner) {
            it?.let { graphqlUser ->
                userBioTv.text = graphqlUser.biography
                supportActionBar?.subtitle = graphqlUser.username
                supportActionBar?.title = graphqlUser.full_name
                userProfile.load(graphqlUser.profile_pic_url_hd)
                userPostTv.text = graphqlUser.edge_owner_to_timeline_media.count.format()
                userFollowTv.text = graphqlUser.edge_followed_by.count.format()
                userFollowingTv.text = graphqlUser.edge_follow.count.format()

                val list = graphqlUser.edge_owner_to_timeline_media.edges.map { UserPostItems(it) }
                userPostItemsAdapter.set(list)
            }
        }

    }


}