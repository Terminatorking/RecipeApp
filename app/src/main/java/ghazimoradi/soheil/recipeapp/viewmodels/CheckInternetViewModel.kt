package ghazimoradi.soheil.recipeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.recipeapp.utils.network.CheckInternetConnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckInternetViewModel @Inject constructor(
    private val checkInternet: CheckInternetConnection
) : ViewModel() {

    private val _isNetworkAvailable = MutableStateFlow(checkInternet.isCurrentlyConnected())
    val isNetworkAvailable: StateFlow<Boolean> get() = _isNetworkAvailable

    init {
        checkConnection()
    }

    private fun checkConnection() {
        viewModelScope.launch {
            checkInternet.observeConnection().collect { isConnected ->
                _isNetworkAvailable.value = isConnected
            }
        }
    }
}