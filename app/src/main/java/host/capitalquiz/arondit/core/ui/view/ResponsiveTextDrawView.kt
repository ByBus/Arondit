package host.capitalquiz.arondit.core.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import host.capitalquiz.arondit.R
import kotlin.math.roundToInt

class ResponsiveTextDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val textBounds = Rect()
    private var text = ""
    private var initTextSize = 0f
    private var minimumTextSize = 0f

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.ResponsiveTextDrawView) {
            text = getString(R.styleable.ResponsiveTextDrawView_textToDraw) ?: ""
            initTextSize = getDimension(R.styleable.ResponsiveTextDrawView_normalTextSize, 22f)
            paint.color = getColor(R.styleable.ResponsiveTextDrawView_textBaseColor, Color.WHITE)
            minimumTextSize = getDimension(R.styleable.ResponsiveTextDrawView_minTextSize, 18f)
            val fontId = getResourceId(R.styleable.ResponsiveTextDrawView_font, 0)
            if (fontId != 0) {
                paint.typeface = ResourcesCompat.getFont(getContext(), fontId)
            }
        }
    }

    fun setText(str: String) {
        text = str
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.textSize = initTextSize
        val availableSpace = width - paddingStart - paddingRight
        val textX = width / 2
        var textY = (height + paddingTop - paddingBottom) / 2

        var lineOfText = text
        val oneLineMinTextSize = initTextSize * 0.5f
        recalculateTextSize(lineOfText, availableSpace, oneLineMinTextSize)
        var lineOffset = (paint.descent() + paint.ascent()) / 2

        if (textBounds.width() > availableSpace) {
            paint.textSize = initTextSize
            lineOfText = text.substring(0 until text.length / 2)
            recalculateTextSize(lineOfText, availableSpace, minimumTextSize)

            val secondLine = text.substring(text.length / 2 until text.length)
            lineOffset = paint.fontSpacing * 0.4f
            textY += paint.fontMetrics.descent.toInt()
            canvas.drawText(secondLine, textX.toFloat(), textY.toFloat() + lineOffset, paint)
        }

        canvas.drawText(lineOfText, textX.toFloat(), textY.toFloat() - lineOffset, paint)
    }

    private fun recalculateTextSize(text: String, availableSpace: Int, minSize: Float) {
        paint.getTextBounds(text, 0, text.length, textBounds)
        while (paint.textSize > minSize.roundToInt() && availableSpace < textBounds.width()) {
            paint.textSize--
            paint.getTextBounds(text, 0, text.length, textBounds)
        }
    }
}