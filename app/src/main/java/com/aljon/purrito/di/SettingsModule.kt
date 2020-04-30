package com.aljon.purrito.di

import androidx.lifecycle.ViewModel
import com.aljon.purrito.ui.feed.dog.DogFeedViewModel
import com.aljon.purrito.ui.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SettingsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindViewModel(viewModel: SettingsViewModel) : ViewModel
}