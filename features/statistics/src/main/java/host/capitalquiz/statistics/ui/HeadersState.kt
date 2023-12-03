package host.capitalquiz.statistics.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import host.capitalquiz.statistics.R

interface HeadersState {
    fun update(headerLayout: LinearLayout)

    abstract class Base : HeadersState {
        abstract val headerId: Int?
        abstract fun iconRes(): Int
        private val defaultDrawable = R.drawable.sort_default_24

        override fun update(headerLayout: LinearLayout) {
            val default = Drawable(headerLayout.context, defaultDrawable)
            val updated = Drawable(headerLayout.context, iconRes())
            headerLayout.children.forEach {
                val drawable = if (it.id == headerId) updated else default
                (it as TextView).setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )
            }
        }

        private fun Drawable(
            context: Context,
            drawableRes: Int,
        ): Drawable? = AppCompatResources.getDrawable(context, drawableRes)
    }

    object Default : Base() {
        override val headerId: Int? = null
        override fun iconRes(): Int = R.drawable.sort_default_24
    }

    class Ascendant(override val headerId: Int) : Base() {
        override fun iconRes(): Int = R.drawable.sort_ascendant_24
    }

    class Descendant(override val headerId: Int) : Base() {
        override fun iconRes(): Int = R.drawable.sort_descendant_24
    }
}