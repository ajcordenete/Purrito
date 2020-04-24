package com.aljon.purrito.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aljon.purrito.Event
import com.aljon.purrito.util.Constants
import com.aljon.purrito.util.SharedPreferenceUtil
import timber.log.Timber
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val sharedPref: SharedPreferenceUtil) : ViewModel() {

    //two way binding for switch
    val isDarkModeOn = MutableLiveData<Boolean>()

    private val _onSaveDarkModeEvent = MutableLiveData<Event<Boolean>>()
    val onSaveDarkModeEvent: LiveData<Event<Boolean>> get() = _onSaveDarkModeEvent

    init {
        isDarkModeOn.value = sharedPref.getValue(Constants.PREF_KEY_DARK_MODE_ON)
    }

    fun saveDarkModeOn(value: Boolean) {
        sharedPref.putBoolean(Constants.PREF_KEY_DARK_MODE_ON, value)
        _onSaveDarkModeEvent.value = Event(value)
    }
}