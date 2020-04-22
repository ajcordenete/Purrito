package com.aljon.purrito.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.aljon.peekfolio.MainCoroutineRule
import com.aljon.purrito.FakeFailingRemoteDataSource
import com.aljon.purrito.FeedType
import com.aljon.purrito.data.Result
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.data.local.PurritoDatabase
import com.aljon.purrito.data.remote.FakeRemoteDataSource
import com.aljon.purrito.data.remote.cat.CatDataSource
import com.aljon.purrito.data.succeeded
import com.aljon.purrito.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class FeedRepositoryImplTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PurritoDatabase

    private val feed1 = Feed("abc", "https://cdn2.thecatapi.com/images/abc.gif", "", 1)
    private val feed2 = Feed("efg", "https://cdn2.thecatapi.com/images/efg.gif", "", 2)
    private val feed3 = Feed("hij", "https://cdn2.thecatapi.com/images/hij.gif", "", 3)

    private val catFeed = listOf<Feed>(feed1, feed2)
    private val dogFeed = listOf<Feed>(feed2, feed3)

    private lateinit var catDataSource: FakeRemoteDataSource
    private lateinit var dogDataSource: FakeRemoteDataSource

    //class under test...
    private lateinit var feedRepository: FeedRepository

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PurritoDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        catDataSource = FakeRemoteDataSource(catFeed)
        dogDataSource = FakeRemoteDataSource(dogFeed)

        //get a reference to class under test...
        feedRepository = FeedRepositoryImpl(
            catDataSource,
            dogDataSource,
            database.feedDao(),
            Dispatchers.Unconfined
        )
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun getFeed_TypeCat() = runBlockingTest {
        //Given a feed type of Cat
        val feedType = FeedType.CAT

        //When getting feed
        val result = feedRepository.getFeed(feedType, 20)

        //Then data should be equal to expected catFeed list
        assertTrue(result.succeeded)
        result as Result.Success
        assertThat(result.data, `is`(catFeed))
    }

    @Test
    fun getFeed_TypeCat_EmptySource() = runBlockingTest {
        //Given a feed type of Cat
        val feedType = FeedType.CAT

        //and empty data source
        val emptySource = FakeRemoteDataSource()
        val feedRepository = FeedRepositoryImpl(emptySource, emptySource, database.feedDao(), Dispatchers.Unconfined)

        //When getting feed
        val result = feedRepository.getFeed(feedType, 20)

        //Then data size should be empty...
        assertTrue(result.succeeded)
        result as Result.Success
        assertThat(result.data.size, `is`(0))
    }

    @Test
    fun getFeed_TypeCat_FailedSource() = runBlockingTest {
        //Given a feed type of Cat
        val feedType = FeedType.CAT

        //and failing data source
        val feedRepository = FeedRepositoryImpl(
            FakeFailingRemoteDataSource,
            FakeFailingRemoteDataSource,
            database.feedDao(),
            Dispatchers.Unconfined
        )

        //When getting feed
        val result = feedRepository.getFeed(feedType, 20)

        //Then data size should be empty...
        assertFalse(result.succeeded)
    }



    @Test
    fun getFeed_TypeDog() = runBlockingTest {
        //Given a feed type of Dog
        val feedType = FeedType.DOG

        //When getting feed
        val result = feedRepository.getFeed(feedType, 20)

        //Then data should be equal to expected dogFeed list
        assertTrue(result.succeeded)
        result as Result.Success
        assertThat(result.data, `is`(dogFeed))
    }

    @Test
    fun getFeed_TypeDog_EmptySource() = runBlockingTest {
        //Given a feed type of Cat
        val feedType = FeedType.DOG

        //and empty data source
        val emptySource = FakeRemoteDataSource()
        val feedRepository = FeedRepositoryImpl(emptySource, emptySource, database.feedDao(), Dispatchers.Unconfined)

        //When getting feed
        val result = feedRepository.getFeed(feedType, 20)

        //Then data size should be empty...
        assertTrue(result.succeeded)
        result as Result.Success
        assertThat(result.data.size, `is`(0))
    }

    @Test
    fun getFeed_TypeDog_FailedSource() = runBlockingTest {
        //Given a feed type of Cat
        val feedType = FeedType.DOG

        //and failing data source
        val feedRepository = FeedRepositoryImpl(
            FakeFailingRemoteDataSource,
            FakeFailingRemoteDataSource,
            database.feedDao(),
            Dispatchers.Unconfined
        )

        //When getting feed
        val result = feedRepository.getFeed(feedType, 20)

        //Then data size should be empty...
        assertFalse(result.succeeded)
    }

    @Test
    fun toggleFavoriteAndGetAllFavorites_NotFavorite() = runBlockingTest {
        //Given a feed
        val feed = feed1

        //When adding to favorites
        val isFavorite = feedRepository.toggleFavorite(feed)

        //and getting all favorites
        val favorites = feedRepository.getAllFavorites()

        //Then favorites contains given feed
        assertThat(favorites.getOrAwaitValue(), hasItems(feed))
        assertTrue(isFavorite)
    }

    @Test
    fun toggleFavoriteAndGetAllFavorites_CurrentFavorite() = runBlockingTest {
        //Given a feed
        val feed = feed1

        //When adding to favorites
        feedRepository.toggleFavorite(feed)

        //and performing toggle again
        val isFavorite = feedRepository.toggleFavorite(feed)

        //and getting all favorites
        val favorites = feedRepository.getAllFavorites()

        //Then favorites should not contain the given feed
        assertThat(favorites.getOrAwaitValue(), not(hasItems(feed)))
        assertFalse(isFavorite)
    }

    @Test
    fun toggleFavoriteAndCheckIfExisting_ToggleOnce() = runBlockingTest {
        //Given a feed
        val feed = feed1

        //When adding to favorites
        feedRepository.toggleFavorite(feed)

        //and getting all favorites
        val existing = feedRepository.isExisting(feed.id)

        //Then feed exists in favorites
        assertTrue(existing)
    }

    @Test
    fun toggleFavoriteAndCheckIfExisting_ToggleTwice() = runBlockingTest {
        //Given a feed
        val feed = feed1

        //When adding to favorites
        feedRepository.toggleFavorite(feed)

        //and performing toggle again
        feedRepository.toggleFavorite(feed)

        //and getting all favorites
        val existing = feedRepository.isExisting(feed.id)

        //Then feed exists in favorites
        assertFalse(existing)
    }


}