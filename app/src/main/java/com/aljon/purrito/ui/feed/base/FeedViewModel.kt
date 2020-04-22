package com.aljon.purrito.ui.feed.base

import androidx.lifecycle.*
import com.aljon.purrito.Event
import com.aljon.purrito.FeedType
import com.aljon.purrito.data.Result
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.repository.FeedRepository
import com.aljon.purrito.ui.BaseViewModel
import kotlinx.coroutines.launch

/**
 * Base viewModel for getting a feed...
 */
open class FeedViewModel(private val feedRepository: FeedRepository, private val feedType: FeedType) : BaseViewModel() {

    private val _feedList = MutableLiveData<MutableList<Feed?>>()
    val feedList: LiveData<MutableList<Feed?>> get() = _feedList

    private val _onLoadMoreDataComplete = MutableLiveData<Event<Boolean>>()
    val onLoadMoreDataComplete: LiveData<Event<Boolean>> get() = _onLoadMoreDataComplete

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    open protected val connectionErrorMessage: String = ""

    init {
        loadInitial()
    }

    private fun loadInitial() = viewModelScope.launch {
        _isLoading.value = true

        val result = feedRepository.getFeed( feedType,20)

        _isLoading.value = false

        if(result is Result.Success) {
            _feedList.value = result.data.toMutableList()
        } else {
            setErrorMessage(connectionErrorMessage)
        }
    }

    fun loadMore() = viewModelScope.launch {
        _feedList.value?.add(null)

        val result = feedRepository.getFeed(feedType,20)
        if(result is Result.Success) {
            _feedList.value?.addAll(result.data.toMutableList())
        } else {
            setErrorMessage(connectionErrorMessage)
        }

        _feedList.value = _feedList.value?.filterNotNull()?.toMutableList()
        _onLoadMoreDataComplete.value = Event(true)
    }

    fun refresh() {
        loadInitial()
    }

}