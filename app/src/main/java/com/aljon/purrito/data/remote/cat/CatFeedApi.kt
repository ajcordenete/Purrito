package com.aljon.purrito.data.remote.cat

import com.aljon.purrito.data.remote.FeedDTO
import com.aljon.purrito.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CatFeedApi {

    @GET("v1/images/search")
    suspend fun getFeed(@Query("limit") limit: Int = Constants.PAGE_LIMIT) : Response<List<FeedDTO>>
}