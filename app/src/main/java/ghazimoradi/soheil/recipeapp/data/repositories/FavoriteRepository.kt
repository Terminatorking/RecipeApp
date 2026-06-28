package ghazimoradi.soheil.recipeapp.data.repositories

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ghazimoradi.soheil.recipeapp.data.source.LocalDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class FavoriteRepository @Inject constructor(localDataSource: LocalDataSource) {
    val local = localDataSource
}