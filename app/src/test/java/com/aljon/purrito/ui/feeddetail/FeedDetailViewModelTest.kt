package com.aljon.purrito.ui.feeddetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aljon.peekfolio.MainCoroutineRule
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.getOrAwaitValue
import com.aljon.purrito.repository.FakeFeedRepository
import com.aljon.purrito.ui.favorite.FavoritesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FeedDetailViewModelTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var feedRepository: FakeFeedRepository

    //class under test...
    private lateinit var viewModel: FeedDetailViewModel

    private val feed = Feed("abc", "https://cdn2.thecatapi.com/images/abc.gif", "", 1)

    @Before
    fun setup() {
        feedRepository = FakeFeedRepository()

        viewModel = FeedDetailViewModel(feedRepository)
    }

    @Test
    fun initFeed() = runBlockingTest {
        //Given a fresh viewModel (See @Before)
        //and a given feed
        val givenFeed = feed

        //when performing initFeed
        viewModel.initFeed(feed.id, feed.url)
        val feedResult = viewModel.feed.getOrAwaitValue()
        val isFavorite = viewModel.isFavorite.getOrAwaitValue()

        //Then result is equal to givenFeed
        assertThat(feedResult.id, `is`(givenFeed.id))
        assertThat(feedResult.url, `is`(givenFeed.url))
        assertThat(feedResult.type, `is`(givenFeed.type))
        assertFalse(isFavorite)
    }

    @Test
    fun toggleFavorite_ToggleOnce() = runBlockingTest {
        //Given a fresh viewModel (See @Before)
        //and a given feed
        val givenFeed = feed

        //when performing initFeed
        viewModel.initFeed(feed.id, feed.url)

        //and toggleFavorite once
        viewModel.toggleFavorite()

        //Then feed is favorite
        assertTrue(viewModel.isFavorite.getOrAwaitValue())
    }

    @Test
    fun toggleFavorite_ToggleTwice() = runBlockingTest {
        //Given a fresh viewModel (See @Before)
        //and a given feed
        val givenFeed = feed

        //when performing initFeed
        viewModel.initFeed(feed.id, feed.url)

        //and toggleFavorite once
        viewModel.toggleFavorite()

        //check if feed is favorite
        assertTrue(viewModel.isFavorite.getOrAwaitValue())

        //and performing toggle again
        viewModel.toggleFavorite()

        //Then feed is removed from favorites
        assertFalse(viewModel.isFavorite.getOrAwaitValue())
    }

    @Test
    fun performShare() = runBlockingTest {
        //Given a fresh viewModel (See @Before)

        //and a given feed
        val givenFeed = feed

        //when performing initFeed
        viewModel.initFeed(feed.id, feed.url)

        //and performing share
        viewModel.shareFeed()

        //Then share event is not null
        assertNotNull(viewModel.shareEvent.getOrAwaitValue())
    }
}