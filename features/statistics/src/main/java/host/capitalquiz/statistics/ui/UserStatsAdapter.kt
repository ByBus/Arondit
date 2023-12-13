package host.capitalquiz.statistics.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import host.capitalquiz.statistics.databinding.TableRowItemBinding

typealias RowBinding = TableRowItemBinding

class UserStatsAdapter(
    override val evenRowColorId: Int,
    override val oddRowColorId: Int,
) : BaseTableRowsAdapter<RowBinding, UserStatsUi>() {

    override fun viewHolder(binding: RowBinding): Bindable = Row(binding)

    override fun binding(inflater: LayoutInflater, parent: ViewGroup): RowBinding =
        RowBinding.inflate(inflater, parent, false)

    private inner class Row(private val binding: RowBinding) : Bindable(binding) {
        override fun bind(item: UserStatsUi, color: Int) {
            super.bind(item, color)
            item.update(binding)
        }
    }

    override fun areItemsSame(oldItem: UserStatsUi, newItem: UserStatsUi): Boolean =
        oldItem.playerName == newItem.playerName
}