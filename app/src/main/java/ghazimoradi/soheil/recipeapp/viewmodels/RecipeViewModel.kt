package ghazimoradi.soheil.recipeapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.recipeapp.data.models.recipe.ResponseRecipes
import ghazimoradi.soheil.recipeapp.data.repositories.RecipeRepository
import ghazimoradi.soheil.recipeapp.utils.ADD_RECIPE_INFORMATION
import ghazimoradi.soheil.recipeapp.utils.API_KEY
import ghazimoradi.soheil.recipeapp.utils.LIMITED_COUNT
import ghazimoradi.soheil.recipeapp.utils.MY_API_KEY
import ghazimoradi.soheil.recipeapp.utils.NUMBER
import ghazimoradi.soheil.recipeapp.utils.POPULARITY
import ghazimoradi.soheil.recipeapp.utils.SORT
import ghazimoradi.soheil.recipeapp.utils.TRUE
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest
import ghazimoradi.soheil.recipeapp.utils.network.NetworkResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
    val popularData = MutableLiveData<NetworkRequest<ResponseRecipes>>()

    //Queries
    fun popularQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[API_KEY] = MY_API_KEY
        queries[SORT] = POPULARITY
        queries[NUMBER] = LIMITED_COUNT.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        return queries
    }

    fun callPopularApi(queries: Map<String, String>) {
        viewModelScope.launch {
            try {
                popularData.value = NetworkRequest.Loading()
                val response = repository.remote.getRecipes(queries)
                popularData.value = NetworkResponse(response).generalNetworkResponse()
            } catch (e: Exception) {
                popularData.value = NetworkRequest.Error(e.message.toString())
            }
        }
    }
}