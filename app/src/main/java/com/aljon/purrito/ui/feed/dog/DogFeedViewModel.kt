package com.aljon.purrito.ui.feed.dog

import com.aljon.purrito.FeedType
import com.aljon.purrito.repository.FeedRepository
import com.aljon.purrito.ui.feed.base.FeedViewModel
import com.aljon.purrito.util.Constants
import javax.inject.Inject

/**
 * viewModel for getting a feed specific for dogs...
 * Sends a dogRepository to the base view model
 */
class DogFeedViewModel @Inject constructor(feedRepository: FeedRepository) : FeedViewModel(feedRepository, FeedType.DOG) {

    override val connectionErrorMessage: String
        get() = Constants.CONNECTION_ERROR_DOG
}