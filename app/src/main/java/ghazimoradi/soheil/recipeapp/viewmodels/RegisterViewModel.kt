package ghazimoradi.soheil.recipeapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.recipeapp.data.models.register.BodyRegister
import ghazimoradi.soheil.recipeapp.data.models.register.ResponseRegister
import ghazimoradi.soheil.recipeapp.data.repositories.RegisterRepository
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest
import ghazimoradi.soheil.recipeapp.utils.network.NetworkResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: RegisterRepository
) : ViewModel() {

    val registerData = MutableLiveData<NetworkRequest<ResponseRegister>>()

    val readData = repository.readRegisterData

    fun callRegisterApi(apiKey: String, body: BodyRegister) {
        viewModelScope.launch {
            try {
                registerData.value = NetworkRequest.Loading()
                val response = repository.postRegister(apiKey, body)
                registerData.value = NetworkResponse(response).generalNetworkResponse()
            } catch (e: Exception) {
                registerData.value = NetworkRequest.Error(e.message.toString())
            }
        }
    }

    fun saveData(username: String, hash: String) {
        viewModelScope.launch {
            repository.saveRegisterData(username, hash)
        }
    }
}