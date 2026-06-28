package ghazimoradi.soheil.recipeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail.AnalyzedInstruction.Step
import ghazimoradi.soheil.recipeapp.databinding.ItemStepBinding
import ghazimoradi.soheil.recipeapp.utils.STEPS_COUNT
import ghazimoradi.soheil.recipeapp.utils.base.BaseAdapter
import ghazimoradi.soheil.recipeapp.utils.minToHour
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class StepsAdapter @Inject constructor() : BaseAdapter<ItemStepBinding, Step>() {

    override val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> ItemStepBinding
        get() = ItemStepBinding::inflate

    override fun bindData(item: Step) {
        binding.apply {
            stepTxt.text = "${adapterPosition + 1}"

            item.length?.let {
                timeTxt.text = item.length.number!!.minToHour()
            }

            infoTxt.text = item.step
        }
    }

    override fun getItemCount(): Int = STEPS_COUNT
}