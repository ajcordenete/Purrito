package com.aljon.purrito.data.remote

import com.aljon.purrito.data.Result
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.data.remote.RemoteDataSource
import org.junit.Assert.*
import java.lang.Exception

class FakeRemoteDataSource(private val feedList: List<Feed>? = listOf()): RemoteDataSource {

    override suspend fun getFeed(page: Int): Result<List<Feed>> {
        feedList?.let {
            return Result.Success(feedList)
        }
        return Result.Error(Exception("Feed not found"))
    }
}