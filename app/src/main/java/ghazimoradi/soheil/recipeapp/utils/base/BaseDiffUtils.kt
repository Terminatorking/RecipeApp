package ghazimoradi.soheil.recipeapp.utils.base

import androidx.recyclerview.widget.DiffUtil.Callback

class BaseDiffUtils<T>(
    private val oldItems: List<T>,
    private val newItems: List<T>,
) : Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldItems[oldItemPosition] === newItems[newItemPosition]

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean = oldItems[oldItemPosition] === newItems[newItemPosition]
}