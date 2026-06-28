package ghazimoradi.soheil.recipeapp.data.repositories

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ghazimoradi.soheil.recipeapp.data.source.RemoteDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class SearchRepository @Inject constructor(private val remote: RemoteDataSource) {
    suspend fun getSearchRecipe(queries: Map<String, String>) = remote.getRecipes(queries)
}