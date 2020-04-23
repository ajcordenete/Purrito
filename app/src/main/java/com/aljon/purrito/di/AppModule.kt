package com.aljon.purrito.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.aljon.purrito.data.local.FeedDao
import com.aljon.purrito.data.local.PurritoDatabase
import com.aljon.purrito.data.remote.cat.CatFeedApi
import com.aljon.purrito.data.remote.dog.DogFeedApi
import com.aljon.purrito.util.Constants
import com.aljon.purrito.util.SharedPreferenceUtil
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
object AppModule {

    @JvmStatic
    @Provides
    @Singleton
    @Named("retrofitCat")
    fun providesCatRetrofit() : Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_CAT)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    @Named("retrofitDog")
    fun providesDogRetrofit() : Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_DOG)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideCatFeedApi(@Named("retrofitCat") retrofit: Retrofit): CatFeedApi {
        return retrofit.create(CatFeedApi::class.java)
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideDogFeedApi(@Named("retrofitDog") retrofit: Retrofit): DogFeedApi {
        return retrofit.create(DogFeedApi::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): PurritoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PurritoDatabase::class.java,
            "Purrito.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideFeedDao(database: PurritoDatabase): FeedDao {
        return database.feedDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideSharedPreferenceUtil(sharedPreferences: SharedPreferences) : SharedPreferenceUtil {
        return SharedPreferenceUtil(sharedPreferences)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}