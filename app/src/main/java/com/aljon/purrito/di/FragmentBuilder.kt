package com.aljon.purrito.di

import com.aljon.purrito.ui.favorite.FavoritesFragment
import com.aljon.purrito.ui.feed.cat.CatFeedFragment
import com.aljon.purrito.ui.feed.base.FeedFragment
import com.aljon.purrito.ui.feed.dog.DogFeedFragment
import com.aljon.purrito.ui.feeddetail.FeedDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector(modules = [CatModule::class])
    abstract fun catFeedFragment(): CatFeedFragment

    @ContributesAndroidInjector(modules = [DogModule::class])
    abstract fun dogFeedFragment(): DogFeedFragment

    @ContributesAndroidInjector(modules = [FeedDetailModule::class])
    abstract fun feedDetailFragment(): FeedDetailFragment

    @ContributesAndroidInjector(modules = [FavoritesModule::class])
    abstract fun favoritesFragment(): FavoritesFragment
}