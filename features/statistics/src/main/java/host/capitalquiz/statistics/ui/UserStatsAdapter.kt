package host.capitalquiz.statistics.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import host.capitalquiz.statistics.R
import host.capitalquiz.statistics.databinding.TableRowItemBinding

class UserStatsAdapter : ListAdapter<UserStatsUi, UserStatsAdapter.Bindable>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Bindable {
        val binding =
            TableRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserStatsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Bindable, position: Int) {
        holder.bind(getItem(position), itemCount - position)
    }

    abstract class Bindable(itemView: View) : ViewHolder(itemView) {
        abstract fun bind(item: UserStatsUi, position: Int)
    }

    inner class UserStatsViewHolder(private val binding: TableRowItemBinding) :
        Bindable(binding.root) {
        override fun bind(item: UserStatsUi, position: Int) {
            item.update(binding)
            binding.root.background = ContextCompat.getDrawable(
                binding.root.context,
                if (position % 2 == 0)
                    R.color.even_row_color
                else
                    R.color.odd_row_color
            )
        }
    }

    companion object {
        private val DIFF_UTIL = object : ItemCallback<UserStatsUi>() {
            override fun areItemsTheSame(oldItem: UserStatsUi, newItem: UserStatsUi): Boolean =
                oldItem.playerName == newItem.playerName

            override fun areContentsTheSame(oldItem: UserStatsUi, newItem: UserStatsUi): Boolean =
                false

        }
    }
}