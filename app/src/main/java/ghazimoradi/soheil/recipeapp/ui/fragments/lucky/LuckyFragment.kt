package ghazimoradi.soheil.recipeapp.ui.fragments.lucky

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.databinding.FragmentLuckyBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment

@AndroidEntryPoint
class LuckyFragment : BaseFragment<FragmentLuckyBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentLuckyBinding
        get() = FragmentLuckyBinding::inflate

}