package ghazimoradi.soheil.recipeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import ghazimoradi.soheil.recipeapp.data.models.recipe.ResponseRecipes.Result
import ghazimoradi.soheil.recipeapp.databinding.ItemPopularBinding
import ghazimoradi.soheil.recipeapp.utils.NEW_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.OLD_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.base.BaseAdapter
import ghazimoradi.soheil.recipeapp.utils.loadImage
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class PopularAdapter @Inject constructor() : BaseAdapter<ItemPopularBinding, Result>() {

    override val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> ItemPopularBinding
        get() = ItemPopularBinding::inflate

    private var onItemClickListener: ((Int) -> Unit)? = null

    override fun bindData(item: Result) {
        binding.apply {
            root.setOnClickListener {
                onItemClickListener?.let { it(item.id!!) }
            }
            popularNameTxt.text = item.title
            popularPriceTxt.text = "${item.pricePerServing} $"
            val imageSplit = item.image!!.split("-")
            val imageSize = imageSplit[1].replace(OLD_IMAGE_SIZE, NEW_IMAGE_SIZE)
            popularImg.loadImage("${imageSplit[0]}-$imageSize")
        }
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}