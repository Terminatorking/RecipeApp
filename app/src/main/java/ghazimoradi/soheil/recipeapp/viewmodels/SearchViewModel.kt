package ghazimoradi.soheil.recipeapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.recipeapp.R.string.something_went_wrong
import ghazimoradi.soheil.recipeapp.data.models.recipe.ResponseRecipes
import ghazimoradi.soheil.recipeapp.data.repositories.SearchRepository
import ghazimoradi.soheil.recipeapp.utils.ADD_RECIPE_INFORMATION
import ghazimoradi.soheil.recipeapp.utils.API_KEY
import ghazimoradi.soheil.recipeapp.utils.FULL_COUNT
import ghazimoradi.soheil.recipeapp.utils.MY_API_KEY
import ghazimoradi.soheil.recipeapp.utils.NUMBER
import ghazimoradi.soheil.recipeapp.utils.QUERY
import ghazimoradi.soheil.recipeapp.utils.TRUE
import ghazimoradi.soheil.recipeapp.utils.getStringResource
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest
import ghazimoradi.soheil.recipeapp.utils.network.NetworkResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    val searchData = MutableLiveData<NetworkRequest<ResponseRecipes>>()

    private fun searchQueries(search: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[API_KEY] = MY_API_KEY
        queries[NUMBER] = FULL_COUNT.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        queries[QUERY] = search
        return queries
    }

    fun callSearchApi(search: String) {
        viewModelScope.launch {
            try {
                searchData.value = NetworkRequest.Loading()
                val response = repository.getSearchRecipe(searchQueries(search))
                searchData.value= NetworkResponse(response).generalNetworkResponse()
            } catch (e: Exception) {
                Log.e("callSearchApi", e.message, e)
                searchData.value = NetworkRequest.Error(getStringResource(something_went_wrong))
            }
        }
    }
}