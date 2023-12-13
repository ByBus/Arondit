package host.capitalquiz.statistics.ui

import android.annotation.SuppressLint
import android.view.View
import android.view.View.OnScrollChangeListener
import android.widget.HorizontalScrollView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

class TableScrollCoordinator(
    headersRow: HorizontalScrollView,
    rowsScroll: HorizontalScrollView,
    playerNamesColumn: RecyclerView,
    rows: RecyclerView,
    motionLayout: MotionLayout,
) {
    private val scrollIdToTag = mutableMapOf<Int, Int>()
    private val headersHorizontalScroll =
        WeakReference(headersRow.apply { scrollIdToTag[id] = HEADERS_HOR_SCROLL })
    private val rowsHorizontalScroll =
        WeakReference(rowsScroll.apply { scrollIdToTag[id] = ROWS_HOR_SCROLL })
    private val playerNamesRc =
        WeakReference(playerNamesColumn.apply { scrollIdToTag[id] = COLUMN_VERT_SCROLL })
    private val allRowsRc = WeakReference(rows.apply { scrollIdToTag[id] = ROWS_VERT_SCROLL })
    private val motionLayoutWeak = WeakReference(motionLayout)
    private var nowScrollingVerticalView = -1
    private var nowScrollingHorizontalView = -1
    private val maxScroll by lazy { playerNamesRc.get()?.width ?: 0 }


    private val horizontalScrollListener =
        OnScrollChangeListener { scrollView, scrollX, _, _, _ ->
            if (nowScrollingHorizontalView != scrollIdToTag[scrollView.id]) return@OnScrollChangeListener

            if (scrollX < maxScroll)
                motionLayoutWeak.get()?.progress = (scrollX.toFloat() / maxScroll).coerceIn(0f, 1f)

            when (nowScrollingHorizontalView) {
                HEADERS_HOR_SCROLL -> rowsHorizontalScroll.get()?.scrollX = scrollX
                ROWS_HOR_SCROLL -> headersHorizontalScroll.get()?.scrollX = scrollX
            }
        }

    private val verticalScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (nowScrollingVerticalView != scrollIdToTag[recyclerView.id]) return
                when (nowScrollingVerticalView) {
                    COLUMN_VERT_SCROLL -> allRowsRc.get()?.scrollBy(dx, dy)
                    ROWS_VERT_SCROLL -> playerNamesRc.get()?.scrollBy(dx, dy)
                }
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    private val setTagHorScrollListener = View.OnTouchListener { view, _ ->
        nowScrollingHorizontalView = scrollIdToTag[view.id] ?: -1
        false
    }

    @SuppressLint("ClickableViewAccessibility")
    private val setTagVertScrollListener = View.OnTouchListener { view, _ ->
        nowScrollingVerticalView = scrollIdToTag[view.id] ?: -1
        false
    }

    init {
        rowsScroll.setOnScrollChangeListener(horizontalScrollListener)
        headersRow.setOnScrollChangeListener(horizontalScrollListener)
        playerNamesColumn.addOnScrollListener(verticalScrollListener)
        rows.addOnScrollListener(verticalScrollListener)

        rowsScroll.setOnTouchListener(setTagHorScrollListener)
        headersRow.setOnTouchListener(setTagHorScrollListener)
        playerNamesColumn.setOnTouchListener(setTagVertScrollListener)
        rows.setOnTouchListener(setTagVertScrollListener)

    }

    companion object {
        private const val HEADERS_HOR_SCROLL = 1
        private const val ROWS_HOR_SCROLL = 2
        private const val COLUMN_VERT_SCROLL = 3
        private const val ROWS_VERT_SCROLL = 4
    }
}