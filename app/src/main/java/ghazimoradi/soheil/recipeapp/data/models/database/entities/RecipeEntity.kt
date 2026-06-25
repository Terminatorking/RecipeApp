package ghazimoradi.soheil.recipeapp.data.models.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ghazimoradi.soheil.recipeapp.data.models.recipe.ResponseRecipes
import ghazimoradi.soheil.recipeapp.utils.RECIPE_TABLE_NAME

@Entity(tableName = RECIPE_TABLE_NAME)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var response: ResponseRecipes
)