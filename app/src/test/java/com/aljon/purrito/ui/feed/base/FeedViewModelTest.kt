package com.aljon.purrito.ui.feed.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.aljon.peekfolio.MainCoroutineRule
import com.aljon.purrito.FeedType
import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.getOrAwaitValue
import com.aljon.purrito.repository.FakeFeedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString

class FeedViewModelTest {

    @get:Rule
    var instantTaskExecutor = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var feedRepository: FakeFeedRepository

    private lateinit var viewModel: FeedViewModel

    //Just placeholder for any feed type, this is not being handled in FakeFeedRepository
    private val anyFeedType = FeedType.CAT

    private val feed = Feed("abc", "https://cdn2.thecatapi.com/images/abc.gif", "", 1)

    @Before
    fun setup() {

        feedRepository = FakeFeedRepository()
        feedRepository.addFeed(feed)

        viewModel = FeedViewModel(feedRepository, anyFeedType)
    }

    @Test
    fun loadInitial_ResultSuccess() {
        //Given a fresh viewModel and fresh repository (See @Before)

        //When loading initial feed list from init{}
        val feedList = viewModel.feedList.getOrAwaitValue()

        //Then
        assertThat(feedList.size, `is`(1))
    }

    @Test
    fun loadInitial_ResultError() {
        //Given a failing feedRepository
        feedRepository.setReturnError(true)

        //and given a fresh viewModel
        viewModel = FeedViewModel(feedRepository, anyFeedType)

        //When loading initial feed list from init{}
        val errorEvent = viewModel.errorMessage.getOrAwaitValue()
        val feedList = viewModel.feedList.value

        //Then
        assertNull(feedList)
        assertNotNull(errorEvent)
    }

    @Test
    fun loadMore_ResultSuccess() {
        //Given a fresh viewModel and fresh repository (See @Before)

        //When calling loadMore once...
        viewModel.loadMore()

        val feedList = viewModel.feedList.getOrAwaitValue()
        val loadMoreComplete = viewModel.onLoadMoreDataComplete.getOrAwaitValue()

        //Then feed list size is doubled
        assertThat(feedList.size, `is`(2))
        assertNotNull(loadMoreComplete)
    }

    @Test
    fun loadMoreTwice_ResultSuccess() {
        //Given a fresh viewModel and fresh repository (See @Before)

        //When calling loadMore twice...
        viewModel.loadMore()
        viewModel.loadMore()

        val feedList = viewModel.feedList.getOrAwaitValue()
        val loadMoreComplete = viewModel.onLoadMoreDataComplete.getOrAwaitValue()

        //Then feed list size is tripled
        assertThat(feedList.size, `is`(3))
        assertNotNull(loadMoreComplete)
    }
}