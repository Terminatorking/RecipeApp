package ghazimoradi.soheil.recipeapp.ui.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import coil3.load
import ghazimoradi.soheil.recipeapp.R.drawable.register_logo
import ghazimoradi.soheil.recipeapp.databinding.FragmentRegisterBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseFragment

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentRegisterBinding
        get() = FragmentRegisterBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            coverImg.load(register_logo)
        }
    }
}