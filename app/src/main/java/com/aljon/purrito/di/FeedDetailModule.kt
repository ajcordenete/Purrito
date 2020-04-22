package com.aljon.purrito.di

import androidx.lifecycle.ViewModel
import com.aljon.purrito.ui.feeddetail.FeedDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FeedDetailModule {

    @Binds
    @IntoMap
    @ViewModelKey(FeedDetailViewModel::class)
    abstract fun bindViewModel(viewModel: FeedDetailViewModel) : ViewModel
}