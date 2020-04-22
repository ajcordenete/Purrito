package com.aljon.purrito.data.remote

import com.aljon.purrito.data.Result
import com.aljon.purrito.data.domain.Feed

interface RemoteDataSource {

    suspend fun getFeed(page: Int) : Result<List<Feed>>
}