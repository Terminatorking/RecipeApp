package ghazimoradi.soheil.recipeapp.ui.fragments.menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable.createFromAttributes
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import ghazimoradi.soheil.recipeapp.R.color.white
import ghazimoradi.soheil.recipeapp.R.style.DarkChip
import ghazimoradi.soheil.recipeapp.databinding.FragmentMenuBinding
import ghazimoradi.soheil.recipeapp.ui.fragments.menu.MenuFragmentDirections.actionMenuToRecipe
import ghazimoradi.soheil.recipeapp.utils.base.BaseBottomSheetDialogFragment
import ghazimoradi.soheil.recipeapp.utils.getColorResource
import ghazimoradi.soheil.recipeapp.utils.onceObserve
import ghazimoradi.soheil.recipeapp.viewmodels.MenuViewModel

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class MenuFragment : BaseBottomSheetDialogFragment<FragmentMenuBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> FragmentMenuBinding
        get() = FragmentMenuBinding::inflate

    private lateinit var viewModel: MenuViewModel

    private var chipCounter = 1
    private var chipMealTitle = ""
    private var chipMealId = 0
    private var chipDietTitle = ""
    private var chipDietId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MenuViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setupChip(viewModel.mealsList(), mealChipGroup)
            setupChip(viewModel.dietsList(), dietChipGroup)

            //Read from menu stored data
            viewModel.readMenuStoredItems.asLiveData().onceObserve(viewLifecycleOwner) {
                chipMealTitle = it.meal
                chipDietTitle = it.diet
                updateChip(it.mealId, mealChipGroup)
                updateChip(it.dietId, dietChipGroup)
            }

            //Meal chips - click
            mealChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                var chip: Chip
                checkedIds.forEach {
                    chip = group.findViewById(it)
                    chipMealTitle = chip.text.toString().lowercase()
                    chipMealId = it
                }
            }

            //Diet chips - click
            dietChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                var chip: Chip
                checkedIds.forEach {
                    chip = group.findViewById(it)
                    chipDietTitle = chip.text.toString().lowercase()
                    chipDietId = it
                }
            }

            submitBtn.setOnClickListener {
                viewModel.saveToStore(chipMealTitle, chipMealId, chipDietTitle, chipDietId)

                actionMenuToRecipe().apply {
                    findNavController().navigate(setIsUpdateData(true))
                }
            }
        }
    }

    private fun setupChip(list: MutableList<String>, view: ChipGroup) {
        list.forEach {
            val chip = Chip(requireContext())

            val drawable = createFromAttributes(
                requireContext(), null, 0, DarkChip
            )

            chip.setChipDrawable(drawable)
            chip.setTextColor(getColorResource(white))
            chip.id = chipCounter++
            chip.text = it
            view.addView(chip)
        }
    }

    private fun updateChip(id: Int, view: ChipGroup) {
        if (id != 0) {
            view.findViewById<Chip>(id).isChecked = true
        }
    }
}