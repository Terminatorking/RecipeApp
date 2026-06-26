package ghazimoradi.soheil.recipeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ghazimoradi.soheil.recipeapp.data.models.recipe.ResponseRecipes.Result
import ghazimoradi.soheil.recipeapp.databinding.ItemPopularBinding
import ghazimoradi.soheil.recipeapp.utils.NEW_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.OLD_IMAGE_SIZE
import ghazimoradi.soheil.recipeapp.utils.base.BaseDiffUtils
import ghazimoradi.soheil.recipeapp.utils.loadImage
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class PopularAdapter @Inject constructor() : Adapter<PopularAdapter.PopularViewHolder>() {

    private lateinit var binding: ItemPopularBinding

    private var items: List<Result> = emptyList()

    private var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        binding = ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder()
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class PopularViewHolder : ViewHolder(binding.root) {

        fun bind(item: Result) {
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
    }

    fun setData(data: List<Result>) {
        val baseDiffUtils = BaseDiffUtils(items, data)
        val result = calculateDiff(baseDiffUtils)
        items = data
        result.dispatchUpdatesTo(this)
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}