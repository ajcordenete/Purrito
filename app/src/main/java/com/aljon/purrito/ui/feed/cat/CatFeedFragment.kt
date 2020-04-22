package com.aljon.purrito.ui.feed.cat

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aljon.purrito.ui.feed.base.FeedFragment
import com.aljon.purrito.ui.feed.base.FeedViewModel

class CatFeedFragment: FeedFragment() {

    //provide CatFeedViewModel in the base FeedFragment
    override val viewModel by viewModels<CatFeedViewModel> { vieModelFactory }
}