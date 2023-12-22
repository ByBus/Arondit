package host.capitalquiz.game.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import host.capitalquiz.game.databinding.WordItemBinding

class WordAdapter(private val wordClickListener: (Long) -> Unit) :
    ListAdapter<WordUi, WordAdapter.BindViewHolder>(DIFF_UTIL) {

    abstract inner class BindViewHolder(itemView: View) : ViewHolder(itemView) {
        protected var id = 0L

        init {
            itemView.setOnClickListener { wordClickListener.invoke(id) }
        }

        abstract fun bind(item: WordUi)
    }

    inner class WordViewHolder(private val binding: WordItemBinding) :
        BindViewHolder(binding.root) {
        override fun bind(item: WordUi) {
            id = item.id
            item.update(binding.eruditWord, binding.wordScores, binding.extraPoints)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        val binding = WordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<WordUi>() {
            override fun areItemsTheSame(oldItem: WordUi, newItem: WordUi): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: WordUi, newItem: WordUi): Boolean {
                return oldItem == newItem
            }
        }
    }
}