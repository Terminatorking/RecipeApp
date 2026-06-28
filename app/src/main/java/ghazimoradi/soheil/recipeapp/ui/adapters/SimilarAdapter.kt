package ghazimoradi.soheil.recipeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import ghazimoradi.soheil.recipeapp.data.models.detail.ResponseSimilar.ResponseSimilarItem
import ghazimoradi.soheil.recipeapp.databinding.ItemSimilarBinding
import ghazimoradi.soheil.recipeapp.utils.BASE_URL_IMAGE_RECIPE
import ghazimoradi.soheil.recipeapp.utils.NEW_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.base.BaseAdapter
import ghazimoradi.soheil.recipeapp.utils.loadImage
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class SimilarAdapter @Inject constructor() :
    BaseAdapter<ItemSimilarBinding, ResponseSimilarItem>() {

    override val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> ItemSimilarBinding
        get() = ItemSimilarBinding::inflate

    private var onItemClickListener: ((Int) -> Unit)? = null

    override fun bindData(item: ResponseSimilarItem) {
        binding.apply {
            nameTxt.text = item.title

            root.setOnClickListener {
                onItemClickListener?.let {
                    it(item.id!!)
                }
            }

            val image = "${BASE_URL_IMAGE_RECIPE}${item.id}-${NEW_IMAGE_SIZE}"
            coverImg.loadImage(image)
        }
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

}