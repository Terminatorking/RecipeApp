package ghazimoradi.soheil.recipeapp.ui.fragments.search

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.databinding.FragmentSearchBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate


}