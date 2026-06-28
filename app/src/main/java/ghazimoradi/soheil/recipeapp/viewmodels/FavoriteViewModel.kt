package ghazimoradi.soheil.recipeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.recipeapp.data.repositories.FavoriteRepository
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    repository: FavoriteRepository
) : ViewModel() {
    val readFavoriteData = repository.local.loadFavorites().asLiveData()
}