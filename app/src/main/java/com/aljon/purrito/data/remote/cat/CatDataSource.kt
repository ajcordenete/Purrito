package com.aljon.purrito.data.remote.cat

import com.aljon.purrito.data.Result
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.data.remoteToDomainModel
import com.aljon.purrito.data.remote.RemoteDataSource
import java.lang.IllegalStateException
import javax.inject.Inject

class CatDataSource @Inject constructor(private val catFeedApi: CatFeedApi):
    RemoteDataSource {

    override suspend fun getFeed(page: Int) : Result<List<Feed>> {
        try {
            var result = catFeedApi.getFeed(page)
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