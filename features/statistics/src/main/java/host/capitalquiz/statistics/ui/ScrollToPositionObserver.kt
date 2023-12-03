package host.capitalquiz.statistics.ui

import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

class ScrollToPositionObserver(recyclerView: RecyclerView, private val position: Int) :
    RecyclerView.AdapterDataObserver() {
    private val recyclerView = WeakReference(recyclerView)

    override fun onChanged() = scrollColumnsToTop()

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) =
        scrollColumnsToTop()

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) =
        scrollColumnsToTop()

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) =
        scrollColumnsToTop()

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) =
        scrollColumnsToTop()

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) =
        scrollColumnsToTop()

    private fun scrollColumnsToTop() {
        recyclerView.get()?.scrollToPosition(position)
    }
}