package com.aljon.purrito.repository

import androidx.lifecycle.LiveData
import com.aljon.purrito.FeedType
import com.aljon.purrito.data.Result
import com.aljon.purrito.data.domain.Feed

interface FeedRepository {

    suspend fun getFeed(feedType: FeedType, limit: Int) : Result<List<Feed>>

    suspend fun toggleFavorite(feed: Feed) : Boolean

    fun getAllFavorites() : LiveData<List<Feed>>

    suspend fun isExisting(id: String): Boolean
}