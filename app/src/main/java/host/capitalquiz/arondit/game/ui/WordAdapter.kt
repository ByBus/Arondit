package host.capitalquiz.arondit.game.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import host.capitalquiz.arondit.databinding.WordItemBinding

class WordAdapter: ListAdapter<String, WordAdapter.BindViewHolder>(DIFF_UTIL) {

    abstract class BindViewHolder(itemView: View): ViewHolder(itemView) {
        abstract fun bind(item: String)
    }

    inner class WordViewHolder(private val binding: WordItemBinding) : BindViewHolder(binding.root) {
        override fun bind(item: String) {
            binding.eruditWord.setText(item)
        }

    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        val binding = WordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}