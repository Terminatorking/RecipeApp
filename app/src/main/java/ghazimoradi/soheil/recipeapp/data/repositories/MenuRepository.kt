package ghazimoradi.soheil.recipeapp.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import ghazimoradi.soheil.recipeapp.data.models.menu.MenuStoredModel
import ghazimoradi.soheil.recipeapp.utils.GLUTEN_FREE
import ghazimoradi.soheil.recipeapp.utils.MAIN_COURSE
import ghazimoradi.soheil.recipeapp.utils.MENU_DIET_ID_KEY
import ghazimoradi.soheil.recipeapp.utils.MENU_DIET_TITLE_KEY
import ghazimoradi.soheil.recipeapp.utils.MENU_MEAL_ID_KEY
import ghazimoradi.soheil.recipeapp.utils.MENU_MEAL_TITLE_KEY
import ghazimoradi.soheil.recipeapp.utils.menuDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class MenuRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private object StoredKey {
        val selectMealTitle = stringPreferencesKey(MENU_MEAL_TITLE_KEY)
        val selectMealId = intPreferencesKey(MENU_MEAL_ID_KEY)
        val selectDietTitle = stringPreferencesKey(MENU_DIET_TITLE_KEY)
        val selectDietId = intPreferencesKey(MENU_DIET_ID_KEY)
    }

    suspend fun saveMenuData(meal: String, mealId: Int, diet: String, dietId: Int) {
        context.menuDataStore.edit {
            it[StoredKey.selectMealTitle] = meal
            it[StoredKey.selectMealId] = mealId
            it[StoredKey.selectDietTitle] = diet
            it[StoredKey.selectDietId] = dietId
        }
    }

    val readMenuData: Flow<MenuStoredModel> = context.menuDataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map {
            val selectMeal = it[StoredKey.selectMealTitle] ?: MAIN_COURSE
            val selectMealId = it[StoredKey.selectMealId] ?: 0
            val selectDiet = it[StoredKey.selectDietTitle] ?: GLUTEN_FREE
            val selectDietId = it[StoredKey.selectDietId] ?: 0
            MenuStoredModel(selectMeal, selectMealId, selectDiet, selectDietId)
        }
}