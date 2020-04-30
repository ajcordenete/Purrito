package com.aljon.purrito.util

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferenceUtil @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getValue(key: String) = sharedPreferences.getBoolean(key, false)
}