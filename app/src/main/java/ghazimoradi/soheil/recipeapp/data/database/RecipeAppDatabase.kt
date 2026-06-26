package ghazimoradi.soheil.recipeapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ghazimoradi.soheil.recipeapp.data.models.database.entities.DetailEntity
import ghazimoradi.soheil.recipeapp.data.models.database.entities.FavoriteEntity
import ghazimoradi.soheil.recipeapp.data.models.database.entities.RecipeEntity

@Database(
    entities = [
        RecipeEntity::class,
        DetailEntity::class,
        FavoriteEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipeAppTypeConverter::class)
abstract class RecipeAppDatabase : RoomDatabase() {
    abstract fun dao(): RecipeAppDao
}