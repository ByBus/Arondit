package host.capitalquiz.core.ui.view.eruditwordview

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback

class WordUpdater<T>(val oldWord: MutableList<T>, private val newWord: List<T>): ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
        for (i in 0 until count) {
            oldWord.add(position + i, newWord[position + i])
        }
    }

    override fun onRemoved(position: Int, count: Int) {
        for (i in 0 until count) {
            oldWord.removeAt(position)
        }
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        val item = oldWord.removeAt(fromPosition)
        oldWord.add(toPosition, item)
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        for (i in 0 until count) {
            oldWord[position + i] = newWord[position + i]
        }
    }

    inner class LetterDiffUtil : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldWord.size

        override fun getNewListSize(): Int = newWord.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldWord[oldItemPosition] == newWord[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldWord[oldItemPosition] == newWord[newItemPosition]
    }

}