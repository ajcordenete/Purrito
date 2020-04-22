package com.aljon.purrito.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aljon.purrito.Event
import com.aljon.purrito.data.domain.Feed

open class BaseViewModel: ViewModel() {

    protected val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = _errorMessage

    protected fun setErrorMessage(errorMessage: String) {
        _errorMessage.value = Event(errorMessage)
    }
}