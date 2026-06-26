package ghazimoradi.soheil.recipeapp.data.source

import ghazimoradi.soheil.recipeapp.data.database.RecipeAppDao
import ghazimoradi.soheil.recipeapp.data.models.database.entities.DetailEntity
import ghazimoradi.soheil.recipeapp.data.models.database.entities.FavoriteEntity
import ghazimoradi.soheil.recipeapp.data.models.database.entities.RecipeEntity
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: RecipeAppDao) {

    suspend fun saveRecipes(entity: RecipeEntity) = dao.saveRecipes(entity)
    fun loadRecipes() = dao.loadRecipes()

    suspend fun saveDetail(entity: DetailEntity) = dao.saveDetail(entity)
    fun loadDetail(id: Int) = dao.loadDetail(id)
    fun existsDetail(id: Int) = dao.existsDetail(id)

    suspend fun saveFavorite(entity: FavoriteEntity) = dao.saveFavorite(entity)
    suspend fun deleteFavorite(entity: FavoriteEntity) = dao.deleteFavorite(entity)
    fun loadFavorites() = dao.loadFavorites()
    fun existsFavorite(id: Int) = dao.existsFavorite(id)
}