package ghazimoradi.soheil.recipeapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.fromHtml
import ghazimoradi.soheil.recipeapp.R.anim.item_anim
import ghazimoradi.soheil.recipeapp.R.color.caribbean_green
import ghazimoradi.soheil.recipeapp.R.color.chineseYellow
import ghazimoradi.soheil.recipeapp.R.color.tart_orange
import ghazimoradi.soheil.recipeapp.R.color.gray
import ghazimoradi.soheil.recipeapp.data.models.recipe.ResponseRecipes.Result
import ghazimoradi.soheil.recipeapp.databinding.ItemRecipesBinding
import ghazimoradi.soheil.recipeapp.utils.NEW_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.OLD_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.base.BaseAdapter
import ghazimoradi.soheil.recipeapp.utils.loadImage
import ghazimoradi.soheil.recipeapp.utils.minToHour
import ghazimoradi.soheil.recipeapp.utils.setDynamicallyColor
import javax.inject.Inject

class RecentAdapter @Inject constructor() : BaseAdapter<ItemRecipesBinding, Result>() {

    override val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> ItemRecipesBinding
        get() = ItemRecipesBinding::inflate

    private var onItemClickListener: ((Int) -> Unit)? = null

    override fun bindData(item: Result) {
        binding.apply {

            recipeNameTxt.text = item.title
            val htmlFormatter = fromHtml(item.summary!!, FROM_HTML_MODE_COMPACT)
            recipeDescTxt.text = htmlFormatter
            recipeLikeTxt.text = item.aggregateLikes.toString()
            recipeTimeTxt.text = item.readyInMinutes!!.minToHour()
            recipeHealthTxt.text = item.healthScore.toString()

            val imageSplit = item.image!!.split("-")
            val imageSize = imageSplit[1].replace(OLD_IMAGE_SIZE, NEW_IMAGE_SIZE)
            recipeImg.loadImage("${imageSplit[0]}-$imageSize")

            recipeVeganTxt.setDynamicallyColor(if (item.vegan!!) caribbean_green else gray)

            when (item.healthScore) {
                in 90..100 -> recipeHealthTxt.setDynamicallyColor(caribbean_green)
                in 60..89 -> recipeHealthTxt.setDynamicallyColor(chineseYellow)
                in 0..59 -> recipeHealthTxt.setDynamicallyColor(tart_orange)
            }

            root.setOnClickListener {
                onItemClickListener?.let { it(item.id!!) }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: BaseAdapter<ItemRecipesBinding, Result>.BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        initAnimation(item_anim)
    }

    override fun onViewDetachedFromWindow(holder: BaseAdapter<ItemRecipesBinding, Result>.BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        clearAnimation()
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

}