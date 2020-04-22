package com.aljon.purrito.ui.favorite

import android.content.Intent
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
import com.aljon.purrito.R
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.databinding.FavoritesFragmentBinding
import com.aljon.purrito.ui.BaseFragment
import com.aljon.purrito.ui.TabFragmentDirections
import com.aljon.purrito.view.SpaceItemDecoration
import com.aljon.purrito.util.autoCleared
import com.aljon.purrito.view.safeNavigate
import com.aljon.purrito.view.showAlertDialog
import com.aljon.purrito.view.waitForTransition
import kotlinx.android.synthetic.main.favorites_fragment.*
import javax.inject.Inject

class FavoritesFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<FavoritesViewModel> { viewModelFactory }

    private var binding by autoCleared<FavoritesFragmentBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    private fun initList() {
        val adapter = FavoriteAdapter(FavoriteAdapter.OnItemSelectListener(
            clickListener = { url, imageView -> navigateToFeedDetail(url.id, url.url, imageView) },
            favoriteListener = { showAlertRemoveFavoriteDialog(it) },
            shareListener = { performShare(it) }
        ))

        viewModel.favorites.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.favoriteList.apply {
            this.adapter = adapter
            addItemDecoration(
                SpaceItemDecoration(
                    requireContext().resources.getDimensionPixelSize(R.dimen.list_item_spacing_large)
                )
            )

            //delay transition from parent fragment since we are using a viewPager
            parentFragment?.waitForTransition(binding.favoriteList)
        }
    }

    private fun performShare(url: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun navigateToFeedDetail(id: String, url: String, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(imageView to url)
        this.findNavController().safeNavigate(TabFragmentDirections.actionTabFragmentToFeedDetailFragment(id, url), extras)
    }

    private fun showAlertRemoveFavoriteDialog(feed: Feed) {
        view?.showAlertDialog(
            dialogTitle = R.string.remove_favorite_title,
            dialogText = R.string.remove_favorite_message,
            actionPositiveText = R.string.remove,
            actionPositive = { viewModel.toggleFavorite(feed) },
            actionNegativeText = R.string.cancel_label,
            actionNegative = {}
        )
    }

    override fun scrollToTop() {
        binding.favoriteList.layoutManager?.smoothScrollToPosition(binding.favoriteList, RecyclerView.State(), 0)
    }
}