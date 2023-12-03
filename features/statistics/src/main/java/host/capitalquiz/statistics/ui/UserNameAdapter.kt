package host.capitalquiz.statistics.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import host.capitalquiz.statistics.R
import host.capitalquiz.statistics.databinding.PlayerNameItemBinding

class UserNameAdapter : ListAdapter<String, UserNameAdapter.StringViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringViewHolder {
        val binding =
            PlayerNameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StringViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StringViewHolder, position: Int) {
        holder.bind(getItem(position), itemCount - position)
    }

    inner class StringViewHolder(private val binding: PlayerNameItemBinding) :
        ViewHolder(binding.root) {
        fun bind(playerName: String, position: Int) {
            binding.playerName.text = playerName
            binding.playerName.background = ContextCompat.getDrawable(
                binding.root.context,
                if (position % 2 == 0)
                    R.color.even_player_row_color
                else
                    R.color.odd_player_row_color
            )
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                false

        }
    }
}