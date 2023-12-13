package host.capitalquiz.statistics.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseTableRowsAdapter<VB : ViewBinding, T> :
    RecyclerView.Adapter<BaseTableRowsAdapter<VB, T>.Bindable>() {

    private val items = mutableListOf<T>()

    abstract val evenRowColorId: Int
    abstract val oddRowColorId: Int

    abstract fun viewHolder(binding: VB): Bindable

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Bindable {
        return viewHolder(binding(LayoutInflater.from(parent.context), parent))
    }

    override fun onBindViewHolder(holder: Bindable, position: Int) {
        val color = if (items.isPositionEven(position)) evenRowColorId else oddRowColorId
        holder.bind(items[position], color)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newList: List<T>) {
        val diffCallback = DiffUtilCallback(items, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    abstract inner class Bindable(binding: VB) : RecyclerView.ViewHolder(binding.root) {
        open fun bind(item: T, color: Int) = itemView.setBackgroundResource(color)
    }

    abstract fun binding(inflater: LayoutInflater, parent: ViewGroup): VB

    private fun List<T>.isPositionEven(position: Int): Boolean = (this.size - position) % 2 == 0

    abstract fun areItemsSame(oldItem: T, newItem: T): Boolean

    private inner class DiffUtilCallback(
        private val oldList: List<T>,
        private val newList: List<T>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val samePosition =
                oldList.isPositionEven(oldItemPosition) == newList.isPositionEven(newItemPosition)
            return samePosition && oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}