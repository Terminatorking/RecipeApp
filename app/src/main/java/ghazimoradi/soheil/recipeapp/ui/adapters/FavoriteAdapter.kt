package ghazimoradi.soheil.recipeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.fromHtml
import ghazimoradi.soheil.recipeapp.R.anim.item_anim
import ghazimoradi.soheil.recipeapp.R.color.caribbean_green
import ghazimoradi.soheil.recipeapp.R.color.chineseYellow
import ghazimoradi.soheil.recipeapp.R.color.gray
import ghazimoradi.soheil.recipeapp.R.color.tart_orange
import ghazimoradi.soheil.recipeapp.data.models.database.entities.FavoriteEntity
import ghazimoradi.soheil.recipeapp.databinding.ItemRecipesBinding
import ghazimoradi.soheil.recipeapp.utils.NEW_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.OLD_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.base.BaseAdapter
import ghazimoradi.soheil.recipeapp.utils.loadImage
import ghazimoradi.soheil.recipeapp.utils.minToHour
import ghazimoradi.soheil.recipeapp.utils.setDynamicallyColor
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class FavoriteAdapter @Inject constructor() : BaseAdapter<ItemRecipesBinding, FavoriteEntity>() {

    override val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> ItemRecipesBinding
        get() = ItemRecipesBinding::inflate

    private var onItemClickListener: ((Int) -> Unit)? = null

    override fun onViewAttachedToWindow(holder: BaseAdapter<ItemRecipesBinding, FavoriteEntity>.BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        initAnimation(item_anim)
    }

    override fun onViewDetachedFromWindow(holder: BaseAdapter<ItemRecipesBinding, FavoriteEntity>.BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        clearAnimation()
    }

    override fun bindData(item: FavoriteEntity) {
        binding.apply {
            item.result.let { result ->

                recipeNameTxt.text = result.title
                val htmlFormatter = fromHtml(result.summary!!, FROM_HTML_MODE_COMPACT)
                recipeDescTxt.text = htmlFormatter
                recipeLikeTxt.text = result.aggregateLikes.toString()
                recipeTimeTxt.text = result.readyInMinutes!!.minToHour()
                recipeHealthTxt.text = result.healthScore.toString()

                val imageSplit = result.image!!.split("-")
                val imageSize = imageSplit[1].replace(OLD_IMAGE_SIZE, NEW_IMAGE_SIZE)
                recipeImg.loadImage("${imageSplit[0]}-$imageSize")

                if (result.vegan!!) {
                    recipeVeganTxt.setDynamicallyColor(caribbean_green)
                } else {
                    recipeVeganTxt.setDynamicallyColor(gray)
                }

                when (result.healthScore) {
                    in 90..100 -> recipeHealthTxt.setDynamicallyColor(caribbean_green)
                    in 60..89 -> recipeHealthTxt.setDynamicallyColor(chineseYellow)
                    in 0..59 -> recipeHealthTxt.setDynamicallyColor(tart_orange)
                }

                root.setOnClickListener {
                    onItemClickListener?.let { it(result.id!!) }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}