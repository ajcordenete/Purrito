package com.aljon.purrito.ui.feed.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aljon.purrito.EventObserver
import com.aljon.purrito.R
import com.aljon.purrito.databinding.FeedFragmentBinding
import com.aljon.purrito.ui.TabFragmentDirections
import com.aljon.purrito.ui.BaseFragment
import com.aljon.purrito.util.Constants.GRID_ITEM_COUNT
import com.aljon.purrito.util.autoCleared
import com.aljon.purrito.view.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

/**
 * Base Fragment for showing generic feed
 * Needs to inherit this class and provide desired viewModel (cat or dog)
 * See (CatFeedFragment or DogFeedFragment)
 */
open class FeedFragment: BaseFragment() {

    @Inject
    protected lateinit var vieModelFactory: ViewModelProvider.Factory

    protected open val viewModel by viewModels<FeedViewModel> { vieModelFactory }

    private var binding by autoCleared<FeedFragmentBinding>()

    private var scrollListener by autoCleared<RecyclerViewLoadMoreScroll>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FeedFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initFeedList()
        initErrorSnackBar()
    }

    private fun initFeedList() {
        val adapter =
            FeedAdapter(FeedAdapter.OnItemSelectListener { feed, imageViewTransition ->
                navigateToFeedDetail(feed.id, feed.url, imageViewTransition)
            })

        val layoutManager = StaggeredGridLayoutManager(GRID_ITEM_COUNT, StaggeredGridLayoutManager.VERTICAL)

        viewModel.feedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.feedList.layoutManager = layoutManager
        binding.feedList.setHasFixedSize(true)
        binding.feedList.adapter = adapter

        binding.feedList.addItemDecoration(
            SpaceItemDecoration(
                requireContext().resources.getDimensionPixelSize(R.dimen.list_item_spacing)
            )
        )

        scrollListener =
            RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                viewModel.loadMore()
            }
        })

        viewModel.onLoadMoreDataComplete.observe(viewLifecycleOwner, EventObserver{success ->
            scrollListener.setLoaded()
        })

        binding.feedList.addOnScrollListener(scrollListener)
    }

    private fun navigateToFeedDetail(id: String, url: String, imageViewTransition: ImageView) {
        val extras = FragmentNavigatorExtras(
            imageViewTransition to imageViewTransition.transitionName
        )
        this.findNavController().safeNavigate(TabFragmentDirections.actionTabFragmentToFeedDetailFragment(id, url), extras)
    }

    private fun initErrorSnackBar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.errorMessage, Snackbar.LENGTH_SHORT)
    }

    override fun scrollToTop() {
        binding.feedList.layoutManager?.smoothScrollToPosition(binding.feedList, RecyclerView.State(), 0)
        binding.feedList.layoutManager?.smoothScrollToPosition(binding.feedList, RecyclerView.State(), 0)
    }
}