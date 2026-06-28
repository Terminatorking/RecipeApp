package ghazimoradi.soheil.recipeapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.recipeapp.data.models.lucky.ResponseLucky
import ghazimoradi.soheil.recipeapp.R.string.something_went_wrong
import ghazimoradi.soheil.recipeapp.data.repositories.LuckyRepository
import ghazimoradi.soheil.recipeapp.utils.ADD_RECIPE_INFORMATION
import ghazimoradi.soheil.recipeapp.utils.API_KEY
import ghazimoradi.soheil.recipeapp.utils.MY_API_KEY
import ghazimoradi.soheil.recipeapp.utils.NUMBER
import ghazimoradi.soheil.recipeapp.utils.TRUE
import ghazimoradi.soheil.recipeapp.utils.getStringResource
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest
import ghazimoradi.soheil.recipeapp.utils.network.NetworkResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LuckyViewModel @Inject constructor(
    private val repository: LuckyRepository
) : ViewModel() {
    val luckyData = MutableLiveData<NetworkRequest<ResponseLucky>>()

    private fun luckyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[API_KEY] = MY_API_KEY
        queries[NUMBER] = "1"
        queries[ADD_RECIPE_INFORMATION] = TRUE
        return queries
    }

    fun callLuckyApi() {
        viewModelScope.launch {
            try {
                luckyData.value = NetworkRequest.Loading()
                val response = repository.getRandomRecipe(luckyQueries())
                luckyData.value = NetworkResponse(response).generalNetworkResponse()
            } catch (e: Exception) {
                Log.e("callLuckyApi", e.message, e)
                luckyData.value = NetworkRequest.Error(getStringResource(something_went_wrong))
            }
        }
    }
}