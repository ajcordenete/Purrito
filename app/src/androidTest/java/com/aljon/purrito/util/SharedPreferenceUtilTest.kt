package com.aljon.purrito.util

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aljon.peekfolio.MainCoroutineRule
import com.aljon.purrito.data.local.PurritoDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SharedPreferenceUtilTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    //class under test...
    private lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    @Before
    fun createSharedPreference() {
        val sharedPreferences = Mockito.mock(SharedPreferences::class.java)
        sharedPreferenceUtil = SharedPreferenceUtil(sharedPreferences)
    }

}