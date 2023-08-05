package host.capitalquiz.arondit.gameslist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import host.capitalquiz.arondit.databinding.GameItemBinding

class GameAdapter(private val callback: (Long) -> Unit): ListAdapter<GameUi, GameAdapter.BindableVH>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableVH {
        val binding = GameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindableVH, position: Int) {
        holder.bind(getItem(position))
    }

    abstract inner class BindableVH(itemView: View) : ViewHolder(itemView) {
        protected var id: Long = 0
        init {
            itemView.setOnClickListener { callback.invoke(id) }
        }
        abstract fun bind(item: GameUi)
    }
    inner class GameViewHolder(private val binding: GameItemBinding): BindableVH(binding.root) {

        override fun bind(item: GameUi) {
            id = item.id
            with(binding){
                val playerViews = listOf(player1, player2, player3, player4)
                item.update(playerViews, dayMonth, year, infoBlock)
            }
        }
    }

    private companion object {
        private val DIFF_UTIL = object : ItemCallback<GameUi>(){
            override fun areItemsTheSame(oldItem: GameUi, newItem: GameUi): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GameUi, newItem: GameUi): Boolean {
                return oldItem == newItem
            }
        }
    }
}