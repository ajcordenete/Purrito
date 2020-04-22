package com.aljon.purrito.ui.feed.cat

import com.aljon.purrito.FeedType
import com.aljon.purrito.repository.FeedRepository
import com.aljon.purrito.ui.feed.base.FeedViewModel
import com.aljon.purrito.util.Constants
import javax.inject.Inject

/**
 * viewModel for getting a feed specific for cats...
 * Sends a CAT feedType to base view model
 */
class CatFeedViewModel @Inject constructor(feedRepository: FeedRepository) : FeedViewModel(feedRepository, FeedType.CAT) {

    override val connectionErrorMessage: String
        get() = Constants.CONNECTION_ERROR_CATS
}