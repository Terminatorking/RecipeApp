package ghazimoradi.soheil.recipeapp.data.models.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail
import ghazimoradi.soheil.recipeapp.utils.DETAIL_TABLE_NAME

@Entity(tableName = DETAIL_TABLE_NAME)
data class DetailEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val result: ResponseDetail
)
