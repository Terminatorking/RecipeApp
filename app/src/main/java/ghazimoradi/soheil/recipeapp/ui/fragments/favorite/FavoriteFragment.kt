package ghazimoradi.soheil.recipeapp.ui.fragments.favorite

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.databinding.FragmentFavoriteBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentFavoriteBinding
        get() = FragmentFavoriteBinding::inflate

}