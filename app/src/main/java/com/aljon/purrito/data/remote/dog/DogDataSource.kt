package com.aljon.purrito.data.remote.dog

import com.aljon.purrito.data.Result
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.data.remoteToDomainModel
import com.aljon.purrito.data.remote.RemoteDataSource
import java.lang.IllegalStateException
import javax.inject.Inject

class DogDataSource @Inject constructor(private val dogFeedApi: DogFeedApi):
    RemoteDataSource {

    override suspend fun getFeed(page: Int): Result<List<Feed>> {
        try {
            var result = dogFeedApi.getFeed(page)
            if(result.isSuccessful) {
                result.body()?.let {
                    return Result.Success(it.remoteToDomainModel())
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return Result.Error(IllegalStateException())
    }
}