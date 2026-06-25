package ghazimoradi.soheil.recipeapp.ui.fragments.recipe

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.databinding.FragmentRecipeBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment

@AndroidEntryPoint
class RecipeFragment : BaseFragment<FragmentRecipeBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentRecipeBinding
        get() = FragmentRecipeBinding::inflate

}