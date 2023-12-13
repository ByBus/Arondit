package host.capitalquiz.statistics.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import host.capitalquiz.statistics.databinding.PlayerNameItemBinding

typealias PlayerNameBinding = PlayerNameItemBinding

class UserNameAdapter(
    override val evenRowColorId: Int,
    override val oddRowColorId: Int,
) : BaseTableRowsAdapter<PlayerNameBinding, String>() {
    override fun viewHolder(binding: PlayerNameBinding): Bindable = Row(binding)

    override fun binding(inflater: LayoutInflater, parent: ViewGroup): PlayerNameBinding =
        PlayerNameBinding.inflate(inflater, parent, false)

    private inner class Row(private val binding: PlayerNameBinding) : Bindable(binding) {
        override fun bind(item: String, color: Int) {
            super.bind(item, color)
            binding.playerName.text = item
        }
    }

    override fun areItemsSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}