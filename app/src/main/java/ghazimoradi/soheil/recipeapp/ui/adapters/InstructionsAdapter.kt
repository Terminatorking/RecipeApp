package ghazimoradi.soheil.recipeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseDetail.ExtendedIngredient
import ghazimoradi.soheil.recipeapp.databinding.ItemInstructionBinding
import ghazimoradi.soheil.recipeapp.utils.BASE_URL_IMAGE_INGREDIENTS
import ghazimoradi.soheil.recipeapp.utils.base.BaseAdapter
import ghazimoradi.soheil.recipeapp.utils.loadImage
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class InstructionsAdapter @Inject constructor() :
    BaseAdapter<ItemInstructionBinding, ExtendedIngredient>() {

    override val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> ItemInstructionBinding
        get() = ItemInstructionBinding::inflate

    override fun bindData(item: ExtendedIngredient) {
        binding.apply {
            nameTxt.text = item.name
            countTxt.text = "${item.amount} ${item.unit}"
            val image = "${BASE_URL_IMAGE_INGREDIENTS}${item.image}"
            coverImg.loadImage(image)
        }
    }
}