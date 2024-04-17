package host.capitalquiz.core.ui

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TwoColumnAutoSpanGridLayoutManager(
    context: Context,
    adapter: RecyclerView.Adapter<*>,
) : GridLayoutManager(context, COLUMNS) {
    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemCount = adapter.itemCount
                return if (itemCount % COLUMNS != 0 && position >= itemCount - 1) COLUMNS else 1
            }
        }
    }

    companion object {
        private const val COLUMNS = 2
    }
}