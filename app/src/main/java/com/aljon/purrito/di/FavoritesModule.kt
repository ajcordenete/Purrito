package com.aljon.purrito.di

import androidx.lifecycle.ViewModel
import com.aljon.purrito.ui.favorite.FavoritesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FavoritesModule {

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    abstract fun bindViewModel(viewModel: FavoritesViewModel) : ViewModel
}