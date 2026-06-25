package ghazimoradi.soheil.recipeapp.data.source

import ghazimoradi.soheil.recipeapp.data.models.register.BodyRegister
import ghazimoradi.soheil.recipeapp.data.network.ApiServices
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val api: ApiServices) {
    suspend fun postRegister(apiKey: String, body: BodyRegister) = api.postRegister(apiKey, body)
    suspend fun getRecipes(queries: Map<String, String>) = api.getRecipes(queries)
    suspend fun getDetail(id: Int, apiKey: String) = api.getDetail(id, apiKey)
    suspend fun getSimilarRecipes(id: Int, apiKey: String) = api.getSimilarRecipes(id, apiKey)
    suspend fun getRandomRecipe(queries: Map<String, String>) = api.getRandomRecipe(queries)
}