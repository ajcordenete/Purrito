package com.aljon.purrito.repository

import androidx.lifecycle.LiveData
import com.aljon.purrito.FeedType
import com.aljon.purrito.data.Result
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.data.local.FeedDao
import com.aljon.purrito.data.remote.RemoteDataSource
import com.aljon.purrito.data.remote.cat.CatDataSource
import com.aljon.purrito.data.remote.dog.DogDataSource
import com.aljon.purrito.di.RepositoryModule.InjectCatDataSource
import com.aljon.purrito.di.RepositoryModule.InjectDogDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class FeedRepositoryImpl @Inject constructor(
    @InjectCatDataSource private val catDataSource: RemoteDataSource,
    @InjectDogDataSource private val dogDataSource: RemoteDataSource,
    private val feedDao: FeedDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : FeedRepository {

    override suspend fun getFeed(feedType: FeedType, page: Int): Result<List<Feed>> {
        return if(feedType == FeedType.CAT) {
             catDataSource.getFeed(page)

        } else {
             dogDataSource.getFeed(page)
        }
    }

    override suspend fun toggleFavorite(feed: Feed): Boolean = withContext(ioDispatcher){
        if(isExisting(feed.id)) {
            //remove favorite if it is currently favorited
            feedDao.removeFavorite(feed.id)
        } else {
            //add to favorites
            feedDao.favorite(feed)
        }

        //check again if it is now existing as favorite in the db
        return@withContext isExisting(feed.id)
    }

    override fun getAllFavorites(): LiveData<List<Feed>> = feedDao.getAllFavorites()

    override suspend fun isExisting(id: String): Boolean = withContext(ioDispatcher) {
        return@withContext feedDao.isExisting(id) > 0
    }
}