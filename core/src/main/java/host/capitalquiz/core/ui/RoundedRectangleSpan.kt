package host.capitalquiz.core.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.text.style.ReplacementSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px

class RoundedRectangleSpan(
    @ColorInt private val color: Int,
    @ColorInt private val textColor: Int,
    @Px private val horizontalPadding: Float,
    private val typeface: Typeface,
    @Px private val fontSize: Float,
    @Px private val radius: Float = 0f,
) : ReplacementSpan() {
    private val rect = RectF()
    private val textBounds = Rect()

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?,
    ): Int {
        paint.typeface = typeface
        paint.textSize = fontSize
        paint.getTextBounds(text.toString(), start, end, textBounds)
        return textBounds.width() + horizontalPadding.toInt() * 2
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint,
    ) {
        rect.set(x, top.toFloat(), x + textBounds.width() + horizontalPadding * 2 , bottom.toFloat())
        paint.color = color
        val radius = this.radius.takeIf { it > 0f } ?: (rect.height() / 2)
        canvas.drawRoundRect(rect, radius, radius, paint)
        paint.color = textColor
        canvas.drawText(text, start, end, x + horizontalPadding, y.toFloat(), paint)
    }
}