package host.capitalquiz.statistics.ui

import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import host.capitalquiz.core.ui.setRightDrawable
import host.capitalquiz.statistics.R


interface HeadersState {
    fun update(headerLayout: LinearLayout)

    abstract class Base : HeadersState {
        abstract val tag: Int
        abstract val headerId: Int?
        private val defaultDrawable = R.drawable.sort_default_24

        override fun update(headerLayout: LinearLayout) {
            val default = AppCompatResources.getDrawable(headerLayout.context, defaultDrawable)
            headerLayout.children.forEach { textView ->
                textView as TextView
                val newState = if (textView.id == headerId) tag else DEFAULT
                val oldState = textView.tag as Int? ?: DEFAULT

                if (oldState != newState)
                    animateIcon(oldState, newState, textView)
                else
                    textView.setRightDrawable(default)

                textView.tag = newState
            }
        }

        abstract fun animatedDrawableResId(oldState: Int): Int

        private fun animateIcon(oldState: Int, newState: Int, textView: TextView) {
            val drawableRes = if (newState == DEFAULT)
                Default.animatedDrawableResId(oldState)
            else
                animatedDrawableResId(oldState)

            val animDrawable = AnimatedVectorDrawableCompat.create(
                textView.context,
                drawableRes
            )
            textView.setRightDrawable(animDrawable)
            animDrawable?.start()
        }
    }

    object Default : Base() {
        override val tag = DEFAULT
        override val headerId: Int? = null
        override fun animatedDrawableResId(oldState: Int): Int {
            return if (oldState == ASC) R.drawable.ascendant_to_default
            else R.drawable.descendant_to_default
        }
    }

    class Ascendant(override val headerId: Int) : Base() {
        override val tag = ASC
        override fun animatedDrawableResId(oldState: Int): Int {
            return if (oldState == DEFAULT) R.drawable.default_to_ascendant
            else R.drawable.descendant_to_ascendant
        }
    }

    class Descendant(override val headerId: Int) : Base() {
        override val tag = DESC
        override fun animatedDrawableResId(oldState: Int): Int {
            return if (oldState == DEFAULT) R.drawable.default_to_descendant
            else R.drawable.ascendant_to_descendant
        }
    }

    companion object {
        private const val DEFAULT = 0
        private const val ASC = 1
        private const val DESC = -1
    }
}