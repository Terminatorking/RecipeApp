package ghazimoradi.soheil.recipeapp.data.network

import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseSimilar
import ghazimoradi.soheil.recipeapp.data.models.lucky.ResponseLucky
import ghazimoradi.soheil.recipeapp.data.models.recipe.ResponseRecipes
import ghazimoradi.soheil.recipeapp.data.models.register.BodyRegister
import ghazimoradi.soheil.recipeapp.data.models.register.ResponseRegister
import ghazimoradi.soheil.recipeapp.utils.API_KEY
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @POST("users/connect")
    suspend fun postRegister(
        @Query(API_KEY) apiKey: String,
        @Body body: BodyRegister
    ): Response<ResponseRegister>

    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<ResponseRecipes>

    @GET("recipes/{id}/information")
    suspend fun getDetail(
        @Path("id") id: Int,
        @Query(API_KEY) apiKey: String
    ): Response<ResponseDetail>

    @GET("recipes/{id}/similar")
    suspend fun getSimilarRecipes(
        @Path("id") id: Int,
        @Query(API_KEY) apiKey: String
    ): Response<ResponseSimilar>

    @GET("recipes/random")
    suspend fun getRandomRecipe(
        @QueryMap queries: Map<String, String>
    ): Response<ResponseLucky>
}