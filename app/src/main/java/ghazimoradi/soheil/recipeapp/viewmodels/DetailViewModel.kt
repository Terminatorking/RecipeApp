package ghazimoradi.soheil.recipeapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.recipeapp.R.string.something_went_wrong
import ghazimoradi.soheil.recipeapp.data.models.database.entities.DetailEntity
import ghazimoradi.soheil.recipeapp.data.models.database.entities.FavoriteEntity
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseSimilar
import ghazimoradi.soheil.recipeapp.data.repositories.RecipeRepository
import ghazimoradi.soheil.recipeapp.utils.getStringResource
import ghazimoradi.soheil.recipeapp.utils.network.NetworkRequest
import ghazimoradi.soheil.recipeapp.utils.network.NetworkResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    repository: RecipeRepository
) : ViewModel() {
    private val local = repository.local
    private val remote = repository.remote

    val detailData = MutableLiveData<NetworkRequest<ResponseDetail>>()
    val similarData = MutableLiveData<NetworkRequest<ResponseSimilar>>()

    val existsDetailData = MutableLiveData<Boolean>()
    val existsFavoriteData = MutableLiveData<Boolean>()

    fun callDetailApi(id: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                detailData.value = NetworkRequest.Loading()
                val response = remote.getDetail(id, apiKey)
                detailData.value = NetworkResponse(response).generalNetworkResponse()

                //Cache
                val cache = detailData.value?.data
                if (cache != null) cacheDetail(cache.id!!, cache)
            } catch (e: Exception) {
                Log.e("callDetailApi", e.message, e)
                detailData.value = NetworkRequest.Error(getStringResource(something_went_wrong))
            }
        }
    }

    private fun cacheDetail(id: Int, response: ResponseDetail) {
        val entity = DetailEntity(id, response)
        saveDetail(entity)
    }

    private fun saveDetail(entity: DetailEntity) {
        viewModelScope.launch {
            local.saveDetail(entity)
        }
    }

    fun readDetailFromDb(id: Int): LiveData<DetailEntity> {
        return local.loadDetail(id).asLiveData()
    }

    fun existsDetail(id: Int) {
        viewModelScope.launch {
            local.existsDetail(id).collect {
                existsDetailData.postValue(it)
            }
        }
    }

    fun callSimilarApi(id: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                similarData.value = NetworkRequest.Loading()
                val response = remote.getSimilarRecipes(id, apiKey)
                similarData.value = NetworkResponse(response).generalNetworkResponse()
            } catch (e: Exception) {
                Log.e("", e.message, e)
                similarData.value = NetworkRequest.Error(getStringResource(something_went_wrong))
            }
        }
    }

    fun saveFavorite(entity: FavoriteEntity) {
        viewModelScope.launch {
            local.saveFavorite(entity)
        }
    }

    fun deleteFavorite(entity: FavoriteEntity) {
        viewModelScope.launch {
            local.deleteFavorite(entity)
        }
    }

    fun existsFavorite(id: Int) {
        viewModelScope.launch {
            local.existsFavorite(id).collect {
                existsFavoriteData.postValue(it)
            }
        }
    }
}