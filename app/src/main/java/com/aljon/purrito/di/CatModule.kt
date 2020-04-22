package com.aljon.purrito.di

import androidx.lifecycle.ViewModel
import com.aljon.purrito.ui.feed.cat.CatFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CatModule {

    @Binds
    @IntoMap
    @ViewModelKey(CatFeedViewModel::class)
    abstract fun bindViewModel(viewModel: CatFeedViewModel) : ViewModel
}