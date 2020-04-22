package com.aljon.purrito.ui.favorite

import androidx.lifecycle.viewModelScope
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.repository.FeedRepository
import com.aljon.purrito.ui.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(private val feedRepository: FeedRepository): BaseViewModel() {

    val favorites = feedRepository.getAllFavorites()

    fun toggleFavorite(feed: Feed) = viewModelScope.launch {
        feedRepository.toggleFavorite(feed)
    }
}