package ghazimoradi.soheil.recipeapp.ui.fragments.steps

import android.view.LayoutInflater
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.databinding.FragmentStepsBinding
import ghazimoradi.soheil.recipeapp.utils.base.BaseBottomSheetDialogFragment

@AndroidEntryPoint
class StepsFragment : BaseBottomSheetDialogFragment<FragmentStepsBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentStepsBinding
        get() = FragmentStepsBinding::inflate

}