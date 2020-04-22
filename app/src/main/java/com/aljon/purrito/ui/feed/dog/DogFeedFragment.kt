package com.aljon.purrito.ui.feed.dog

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aljon.purrito.ui.feed.base.FeedFragment
import com.aljon.purrito.ui.feed.base.FeedViewModel

class DogFeedFragment : FeedFragment() {

    //provide viewModel in the base FeedFragment
    override val viewModel by viewModels<DogFeedViewModel> { vieModelFactory }
}