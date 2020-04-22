package com.aljon.purrito.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aljon.peekfolio.MainCoroutineRule
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.getOrAwaitValue
import com.aljon.purrito.repository.FakeFeedRepository
import com.aljon.purrito.ui.feed.base.FeedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var feedRepository: FakeFeedRepository

    //class under test...
    private lateinit var viewModel: FavoritesViewModel

    private val feed = Feed("abc", "https://cdn2.thecatapi.com/images/abc.gif", "", 1)

    @Before
    fun setup() {

        feedRepository = FakeFeedRepository()
        viewModel = FavoritesViewModel(feedRepository)
    }

    @Test
    fun toggleFavoritesAndGetAllFavorites_ToggleOnce()  {
        //Given a fresh viewModel and fresh repository (See @Before)

        //When toggling a non-existing feed
        viewModel.toggleFavorite(feed)

        //and getting all favorites
        val favorites = viewModel.favorites.getOrAwaitValue()

        //Then feed is added in favorites
        assertThat(favorites.size, `is`(1))
        assertThat(favorites, hasItems(feed))
    }

    @Test
    fun toggleFavoritesAndGetAllFavorites_ToggleTwice()  {
        //Given a fresh viewModel and fresh repository (See @Before)

        //When toggling a non-existing feed twice
        viewModel.toggleFavorite(feed)
        viewModel.toggleFavorite(feed)

        //and getting all favorites
        val favorites = viewModel.favorites.getOrAwaitValue()

        //Then feed is not in favorites
        assertThat(favorites.size, `is`(0))
        assertThat(favorites, not(hasItems(feed)))
    }

}