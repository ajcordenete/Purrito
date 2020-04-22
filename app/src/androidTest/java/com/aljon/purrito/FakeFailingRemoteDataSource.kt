package com.aljon.purrito

import com.aljon.purrito.data.Result
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.data.remote.FakeRemoteDataSource
import com.aljon.purrito.data.remote.RemoteDataSource

object FakeFailingRemoteDataSource: RemoteDataSource {

    override suspend fun getFeed(page: Int): Result<List<Feed>> {
        return Result.Error(Exception("Test"))
    }
}