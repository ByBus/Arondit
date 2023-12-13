package host.capitalquiz.statistics.ui

import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import host.capitalquiz.core.ui.setRightDrawable
import host.capitalquiz.statistics.R


interface HeadersUiState {

    fun update(headerLayout: LinearLayout)
    fun rememberPrevious(state: HeadersUiState)
    fun headerId(): Int?
    fun direction(): Int

    abstract class Base : HeadersUiState {
        protected abstract val headerId: Int?
        protected abstract val direction: Int
        private var previousHeaderId: Int? = null
        private var previousHeaderDir = DEFAULT

        override fun headerId(): Int? = headerId
        override fun direction(): Int = direction

        override fun rememberPrevious(state: HeadersUiState) {
            previousHeaderId = state.headerId()
            previousHeaderDir = state.direction()
        }

        override fun update(headerLayout: LinearLayout) {
            val defaultIcon =
                AppCompatResources.getDrawable(headerLayout.context, defaultDrawableResId)

            headerLayout.children.forEach { textView ->
                textView as TextView
                val startDir = if (textView.id == previousHeaderId) previousHeaderDir else DEFAULT
                val endDir = if (textView.id == headerId) direction else DEFAULT

                if (startDir != endDir)
                    textView.animateRightDrawable(startDir, endDir)
                else
                    textView.setRightDrawable(defaultIcon)
            }
        }

        abstract fun animatedDrawableResId(startDir: Int): Int

        private fun TextView.animateRightDrawable(startDir: Int, endDir: Int) {
            val drawableRes = if (endDir == DEFAULT)
                Default.animatedDrawableResId(startDir)
            else
                animatedDrawableResId(startDir)

            val animDrawable = AnimatedVectorDrawableCompat.create(
                context,
                drawableRes
            )
            setRightDrawable(animDrawable)
            animDrawable?.start()
        }
    }

    object Default : Base() {
        override val direction = DEFAULT
        override val headerId: Int? = null
        override fun animatedDrawableResId(startDir: Int): Int {
            return if (startDir == ASC) R.drawable.ascendant_to_default
            else R.drawable.descendant_to_default
        }
    }

    class Ascendant(override val headerId: Int) : Base() {
        override val direction = ASC
        override fun animatedDrawableResId(startDir: Int): Int {
            return if (startDir == DEFAULT) R.drawable.default_to_ascendant
            else R.drawable.descendant_to_ascendant
        }
    }

    class Descendant(override val headerId: Int) : Base() {
        override val direction = DESC
        override fun animatedDrawableResId(startDir: Int): Int {
            return if (startDir == DEFAULT) R.drawable.default_to_descendant
            else R.drawable.ascendant_to_descendant
        }
    }

    companion object {
        private const val DEFAULT = 0
        private const val ASC = 1
        private const val DESC = -1
        private val defaultDrawableResId = R.drawable.sort_default_24
    }
}