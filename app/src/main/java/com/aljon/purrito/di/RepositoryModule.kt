package com.aljon.purrito.di

import com.aljon.purrito.data.domain.Feed
import com.aljon.purrito.data.remote.RemoteDataSource
import com.aljon.purrito.repository.FeedRepository
import com.aljon.purrito.data.remote.cat.CatDataSource
import com.aljon.purrito.data.remote.dog.DogDataSource
import com.aljon.purrito.repository.FeedRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class InjectDogDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class InjectCatDataSource

    @Binds
    @Singleton
    @InjectDogDataSource
    abstract fun bindDogRepository(dogDataSource: DogDataSource) : RemoteDataSource

    @Binds
    @Singleton
    @InjectCatDataSource
    abstract fun bindCatRepository(catDataSource: CatDataSource) : RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindFeedRepository(repo: FeedRepositoryImpl) : FeedRepository

}