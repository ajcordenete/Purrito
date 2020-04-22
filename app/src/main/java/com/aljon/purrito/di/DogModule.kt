package com.aljon.purrito.di

import androidx.lifecycle.ViewModel
import com.aljon.purrito.ui.feed.dog.DogFeedViewModel
import com.aljon.purrito.ui.feed.base.FeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DogModule {

    @Binds
    @IntoMap
    @ViewModelKey(DogFeedViewModel::class)
    abstract fun bindViewModel(viewModel: DogFeedViewModel) : ViewModel
}