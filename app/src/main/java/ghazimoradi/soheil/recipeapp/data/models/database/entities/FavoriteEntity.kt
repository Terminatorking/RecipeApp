package ghazimoradi.soheil.recipeapp.data.models.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail
import ghazimoradi.soheil.recipeapp.utils.FAVORITE_TABLE_NAME

@Entity(tableName = FAVORITE_TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val result: ResponseDetail
)
