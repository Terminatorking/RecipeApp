package ghazimoradi.soheil.recipeapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.recipeapp.data.models.database.entities.RecipeEntity
import ghazimoradi.soheil.recipeapp.data.models.recipe.ResponseRecipes
import ghazimoradi.soheil.recipeapp.data.repositories.RecipeRepository
import ghazimoradi.soheil.recipeapp.utils.*
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest
import ghazimoradi.soheil.recipeapp.utils.network.NetworkResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    repository: RecipeRepository
) : ViewModel() {

    val remote = repository.remote
    val local = repository.local

    val popularData = MutableLiveData<NetworkRequest<ResponseRecipes>>()
    val recentData = MutableLiveData<NetworkRequest<ResponseRecipes>>()

    val readPopularFromDb: LiveData<List<RecipeEntity>> = local.loadRecipes().asLiveData()

    val readRecentFromDb: LiveData<List<RecipeEntity>> = local.loadRecipes().asLiveData()

    private var mealType = MAIN_COURSE
    private var dietType = GLUTEN_FREE

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
                val response = remote.getRecipes(queries)
                popularData.value = NetworkResponse(response).generalNetworkResponse()

                //Cache
                val cache = popularData.value?.data
                if (cache != null) handleCache(cache, true)
            } catch (e: Exception) {
                popularData.value = NetworkRequest.Error(e.message.toString())
            }
        }
    }

    fun recentQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[API_KEY] = MY_API_KEY
        queries[TYPE] = mealType
        queries[DIET] = dietType
        queries[NUMBER] = FULL_COUNT.toString()
        queries[ADD_RECIPE_INFORMATION] = TRUE
        return queries
    }

    fun callRecentApi(queries: Map<String, String>) {
        viewModelScope.launch {
            try {
                recentData.value = NetworkRequest.Loading()
                val response = remote.getRecipes(queries)
                recentData.value = recentNetworkResponse(response)

                //Cache
                val cache = recentData.value?.data
                if (cache != null) handleCache(cache, false)
            } catch (e: Exception) {
                recentData.value = NetworkRequest.Error(e.message.toString())
            }
        }
    }

    private fun recentNetworkResponse(response: Response<ResponseRecipes>): NetworkRequest<ResponseRecipes> {
        return when {
            response.message().contains("timeout") -> NetworkRequest.Error("Timeout")
            response.code() == 401 -> NetworkRequest.Error("You are not authorized")
            response.code() == 402 -> NetworkRequest.Error("Your free plan finished")
            response.code() == 422 -> NetworkRequest.Error("Api key not found!")
            response.code() == 500 -> NetworkRequest.Error("Try again")
            response.body()!!.results.isNullOrEmpty() -> NetworkRequest.Error("Not found any recipe!")
            response.isSuccessful -> NetworkRequest.Success(response.body()!!)
            else -> NetworkRequest.Error(response.message())
        }
    }

    private fun save(entity: RecipeEntity) {
        viewModelScope.launch(IO) {
            local.saveRecipes(entity)
        }
    }

    private fun handleCache(response: ResponseRecipes, isPopular: Boolean) {
        val entity = RecipeEntity(if (isPopular) 0 else 1, response)
        save(entity)
    }
}