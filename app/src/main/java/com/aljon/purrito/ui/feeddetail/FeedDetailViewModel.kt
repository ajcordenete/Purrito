package com.aljon.purrito.ui.feeddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aljon.purrito.Event
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.repository.FeedRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedDetailViewModel @Inject constructor(private val feedRepository: FeedRepository) : ViewModel() {

    private val _feed = MutableLiveData<Feed>()
    val feed: LiveData<Feed> get() = _feed

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _shareEvent = MutableLiveData<Event<String>>()
    val shareEvent: LiveData<Event<String>> get() = _shareEvent

    private val _downloadEvent = MutableLiveData<Event<String>>()
    val downloadEvent: LiveData<Event<String>> get() = _downloadEvent

    fun initFeed(id: String, url: String) = viewModelScope.launch {
        _feed.value = Feed(id, url)
        _isFavorite.value = feedRepository.isExisting(_feed.value!!.id)
    }

    fun toggleFavorite() = viewModelScope.launch {
        _isFavorite.value = feedRepository.toggleFavorite(_feed.value!!)
    }

    fun shareFeed() {
        _shareEvent.value = Event(_feed.value!!.url)
    }

    fun downloadFeed() {
        _downloadEvent.value = Event(_feed.value!!.url)
    }
}