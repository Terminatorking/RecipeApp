package ghazimoradi.soheil.recipeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ghazimoradi.soheil.recipeapp.data.repositories.MenuRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: MenuRepository
) : ViewModel() {
    val readMenuStoredItems = repository.readMenuData

    fun saveToStore(
        meal: String,
        mealId: Int,
        diet: String,
        dietId: Int,
    ) {
        viewModelScope.launch(IO) {
            repository.saveMenuData(meal, mealId, diet, dietId)
        }
    }

    fun mealsList(): MutableList<String> {
        return mutableListOf(
            "Main Course", "Bread", "Marinade", "Side Dish", "Breakfast", "Dessert", "Soup", "Snack", "Appetizer",
            "Beverage", "Drink", "Salad", "Sauce"
        )
    }

    fun dietsList(): MutableList<String> {
        return mutableListOf("Gluten Free", "Ketogenic", "Vegetarian", "Vegan", "Pescetarian", "Paleo")
    }
}