package ghazimoradi.soheil.recipeapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import ghazimoradi.soheil.recipeapp.data.models.database.entities.DetailEntity
import ghazimoradi.soheil.recipeapp.data.models.database.entities.FavoriteEntity
import ghazimoradi.soheil.recipeapp.data.models.database.entities.RecipeEntity
import ghazimoradi.soheil.recipeapp.utils.DETAIL_TABLE_NAME
import ghazimoradi.soheil.recipeapp.utils.FAVORITE_TABLE_NAME
import ghazimoradi.soheil.recipeapp.utils.RECIPE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeAppDao {

    @Insert(onConflict = REPLACE)
    suspend fun saveRecipes(entity: RecipeEntity)

    @Query("SELECT * FROM $RECIPE_TABLE_NAME ORDER BY ID ASC")
    fun loadRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun saveDetail(entity: DetailEntity)

    @Query("SELECT * FROM $DETAIL_TABLE_NAME WHERE id = :id")
    fun loadDetail(id: Int): Flow<DetailEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM $DETAIL_TABLE_NAME WHERE ID = :id)")
    fun existsDetail(id: Int): Flow<Boolean>

    @Insert(onConflict = REPLACE)
    suspend fun saveFavorite(entity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(entity: FavoriteEntity)

    @Query("SELECT * FROM $FAVORITE_TABLE_NAME ORDER BY ID ASC")
    fun loadFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM $FAVORITE_TABLE_NAME WHERE ID = :id)")
    fun existsFavorite(id: Int): Flow<Boolean>
}