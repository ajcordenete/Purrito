package com.aljon.purrito.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.aljon.purrito.FeedType
import com.aljon.purrito.data.Result
import com.aljon.purrito.data.domain.Feed
import java.util.LinkedHashMap

class FakeFeedRepository: FeedRepository {

    var feedServiceData: MutableList<Feed> = mutableListOf()

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getFeed(feedType: FeedType, limit: Int): Result<List<Feed>> {
        if(shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        return Result.Success(feedServiceData)
    }

    override suspend fun toggleFavorite(feed: Feed): Boolean {
        if(isExisting(feed.id)) {
            feedServiceData.remove(feed)
        } else {
            feedServiceData.add(feed)
        }

        return isExisting(feed.id)
    }

    override fun getAllFavorites(): LiveData<List<Feed>> {
        return MutableLiveData<List<Feed>>(feedServiceData)
    }

    override suspend fun isExisting(id: String): Boolean {
        return feedServiceData.filter { it.id == id }.isNotEmpty()
    }

    @VisibleForTesting
    fun addFeed(vararg feeds: Feed) {
        for (feed in feeds) {
            feedServiceData.add(feed)
        }
    }
}