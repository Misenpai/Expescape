package com.example.expescape.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expescape.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> = _navigationEvent

    fun navigateToTransactionForm() {
        _navigationEvent.value = NavigationEvent.NavigateToTransactionForm
    }

    sealed class NavigationEvent {
        object NavigateToTransactionForm : NavigationEvent()
        // Add other navigation events here as needed
    }
}