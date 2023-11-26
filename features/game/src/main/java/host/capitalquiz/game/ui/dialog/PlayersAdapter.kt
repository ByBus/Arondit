package host.capitalquiz.game.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import host.capitalquiz.game.databinding.PlayerItemBinding
import host.capitalquiz.game.domain.Player

class PlayersAdapter(private val callback: Callback) :
    ListAdapter<Player, PlayersAdapter.PlayerViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = PlayerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlayerViewHolder(private val binding: PlayerItemBinding) :
        ViewHolder(binding.root) {
        private var id: Long = 0L

        init {
            binding.root.setOnClickListener {
                callback.onClick(id)
            }
        }

        fun bind(item: Player) {
            id = item.id
            binding.playerName.text = item.name
        }
    }


    fun interface Callback {
        fun onClick(id: Long)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Player>() {
            override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
                oldItem == newItem
        }
    }
}