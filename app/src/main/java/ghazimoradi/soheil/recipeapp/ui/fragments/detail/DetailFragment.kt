package ghazimoradi.soheil.recipeapp.ui.fragments.detail

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.databinding.FragmentDetailBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentDetailBinding
        get() = FragmentDetailBinding::inflate

}