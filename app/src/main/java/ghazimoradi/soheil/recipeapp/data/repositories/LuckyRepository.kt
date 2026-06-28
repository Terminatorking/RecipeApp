package ghazimoradi.soheil.recipeapp.data.repositories

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ghazimoradi.soheil.recipeapp.data.source.RemoteDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class LuckyRepository @Inject constructor(private val remote: RemoteDataSource) {
    suspend fun getRandomRecipe(queries: Map<String, String>) = remote.getRandomRecipe(queries)
}