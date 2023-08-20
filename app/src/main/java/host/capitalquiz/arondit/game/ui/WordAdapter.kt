package host.capitalquiz.arondit.game.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import host.capitalquiz.arondit.databinding.WordItemBinding

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
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun bind(item: WordUi) {
            id = item.id
            item.update(binding.eruditWord, binding.wordScores)
        }
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        val binding = WordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}