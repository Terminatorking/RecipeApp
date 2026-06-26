package ghazimoradi.soheil.recipeapp.data.repositories

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ghazimoradi.soheil.recipeapp.data.source.RemoteDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class RecipeRepository @Inject constructor(
    remoteDataSource: RemoteDataSource
) {
    val remote = remoteDataSource
}