package host.capitalquiz.editgamerule.ui.editscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import host.capitalquiz.editgamerule.databinding.GameRuleLetterItemBinding

class RuleLetterAdapter : ListAdapter<RuleLetter, RuleLetterAdapter.Bindable>(DIFF_UTIL_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Bindable {
        val binding =
            GameRuleLetterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LetterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Bindable, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class Bindable(itemView: View) : RecyclerView.ViewHolder(itemView) {
        protected var letter: Char = '_'
        abstract fun bind(item: RuleLetter)
    }

    inner class LetterViewHolder(private val binding: GameRuleLetterItemBinding): Bindable(binding.root) {
        override fun bind(item: RuleLetter) {
            letter = item.letter
            binding.letter.updateLetter(letter, item.points)
        }
    }

    companion object {
        private val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<RuleLetter>() {
            override fun areItemsTheSame(oldItem: RuleLetter, newItem: RuleLetter): Boolean {
                return oldItem.letter == newItem.letter
            }

            override fun areContentsTheSame(oldItem: RuleLetter, newItem: RuleLetter): Boolean {
                return oldItem == newItem
            }
        }
    }
}