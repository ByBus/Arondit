package host.capitalquiz.editgamerule.ui.ruleslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import host.capitalquiz.editgamerule.databinding.GameRuleItemBinding

class GameRulesAdapter(private val listener: RuleClickListener) :
    ListAdapter<GameRuleUi, GameRulesAdapter.Bindable>(DIFF_UTIL_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Bindable {
        val binding =
            GameRuleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameRuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Bindable, position: Int) {
        holder.bind(getItem(position))
    }


    abstract class Bindable(itemView: View) : ViewHolder(itemView) {
        protected var id: Long = 0L
        abstract fun bind(item: GameRuleUi)
    }

    inner class GameRuleViewHolder(private val binding: GameRuleItemBinding) :
        Bindable(binding.root) {
        init {
            binding.root.setOnClickListener { listener.onRuleClick(id) }
            binding.deleteRule.setOnClickListener { listener.onDeleteClick(id) }
            binding.root.setOnLongClickListener {
                listener.onEditClick(id)
                true
            }
        }
        override fun bind(item: GameRuleUi) {
            id = item.id
            with(binding) {
                ruleCheckbox.isChecked = item.selected
                ruleName.text = item.name
                deleteRule.isEnabled = item.readOnly.not()
                deleteRule.imageAlpha = if (item.readOnly) 75 else 255
            }
        }
    }

    companion object {
        private val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<GameRuleUi>() {
            override fun areItemsTheSame(oldItem: GameRuleUi, newItem: GameRuleUi): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GameRuleUi, newItem: GameRuleUi): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface RuleClickListener {
        fun onRuleClick(ruleId: Long)

        fun onDeleteClick(ruleId: Long)

        fun onEditClick(ruleId: Long)
    }
}