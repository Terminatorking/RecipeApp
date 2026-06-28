package ghazimoradi.soheil.recipeapp.ui.fragments.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail.AnalyzedInstruction.Step
import ghazimoradi.soheil.recipeapp.databinding.FragmentStepsBinding
import ghazimoradi.soheil.recipeapp.ui.adapters.StepsAdapter
import ghazimoradi.soheil.recipeapp.utils.STEPS_COUNT
import ghazimoradi.soheil.recipeapp.utils.base.BaseBottomSheetDialogFragment
import ghazimoradi.soheil.recipeapp.utils.setupRecyclerview
import javax.inject.Inject

@AndroidEntryPoint
class StepsFragment : BaseBottomSheetDialogFragment<FragmentStepsBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentStepsBinding
        get() = FragmentStepsBinding::inflate

    private val args: StepsFragmentArgs by navArgs()
    private lateinit var steps: MutableList<Step>

    @Inject
    lateinit var stepsAdapter: StepsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            args.data.steps?.let {
                this@StepsFragment.steps = it.toMutableList()
                if (steps.isNotEmpty()) {
                    STEPS_COUNT = steps.size
                    stepsAdapter.setData(steps)
                    stepsList.setupRecyclerview(
                        LinearLayoutManager(requireContext()),
                        stepsAdapter
                    )
                }
            }
        }
    }
}