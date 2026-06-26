package ghazimoradi.soheil.recipeapp.utils.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import androidx.annotation.AnimRes
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<B : ViewBinding, M> : Adapter<BaseAdapter<B, M>.BaseViewHolder>() {

    protected abstract val bindingInflater: (inflater: LayoutInflater, parent: ViewGroup, attach: Boolean) -> B

    private var _binding: B? = null
    protected val binding: B get() = requireNotNull(_binding)

    private var _context: Context? = null
    protected val context: Context get() = requireNotNull(_context)

    private var items: List<M> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        _context = parent.context
        _binding = bindingInflater.invoke(LayoutInflater.from(context), parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseAdapter<B, M>.BaseViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class BaseViewHolder(binding: B) : ViewHolder(binding.root) {
        fun bind(item: M) {
            bindData(item)
        }
    }

    abstract fun bindData(item: M)

    fun setData(data: List<M>) {
        val baseDiffUtils = BaseDiffUtils(items, data)
        val result = calculateDiff(baseDiffUtils)
        items = data
        result.dispatchUpdatesTo(this)
    }

    fun initAnimation(@AnimRes anim: Int) {
        binding.root.animation = loadAnimation(context, anim)
    }

    fun clearAnimation() {
        binding.root.clearAnimation()
    }
}
