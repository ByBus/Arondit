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

private const val ONE_LINE_THRESHOLD_FACTOR = 0.6F

class ResponsiveTextDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private val textBounds = Rect()
    private var text = ""
    private var initTextSize = 0f
    private var minimumTextSize = 0f
    private var availableWidth = 0
    private var availableHeight = 0

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
                paint.typeface = ResourcesCompat.getFont(context, fontId)
            }
        }
    }

    private val oneLineMinTextSize = initTextSize * ONE_LINE_THRESHOLD_FACTOR

    fun setText(str: String) {
        text = str
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (text.isBlank()) return
        paint.textSize = initTextSize
        val textX = width / 2
        val textY = paddingTop + (height - paddingTop - paddingBottom) / 2

        var lineOfText = text

        var twoLinesAdditionalOffset = 0f
        recalculateTextSize(oneLineMinTextSize, lineOfText)
        var verticalOffset = verticalOffset()
        if (isFitInBounds(1).not()) {
            paint.textSize = initTextSize
            val (firstLine, secondLine) = splitLines(text)
            lineOfText = firstLine
            recalculateTextSize(minimumTextSize, firstLine, secondLine)

            verticalOffset = verticalOffset()
            twoLinesAdditionalOffset = lineHeight() / 2
            canvas.drawText(secondLine, textX.toFloat(), textY - verticalOffset + twoLinesAdditionalOffset, paint)
        }

        canvas.drawText(lineOfText, textX.toFloat(), textY - verticalOffset - twoLinesAdditionalOffset, paint)
    }

    private fun verticalOffset() = (paint.descent() + paint.ascent()) / 2
    private fun lineHeight() = paint.descent() - paint.ascent()

    private fun splitLines(text: String): Pair<String, String> {
        return if (text.contains(" ")) {
            text.substringBefore(" ") to
                    text.substringAfter(" ")
        } else {
            text.substring(0 until text.length / 2) to
                    text.substring(text.length / 2 until text.length)
        }
    }

    private fun recalculateTextSize(minSize: Float, vararg lines: String) {
        val longestLine = lines.maxBy { it.length }
        paint.getTextBounds(longestLine, 0, longestLine.length, textBounds)
        while (paint.textSize > minSize.roundToInt() && isFitInBounds(lines.size).not()) {
            paint.textSize--
            paint.getTextBounds(longestLine, 0, longestLine.length, textBounds)
        }
    }

    private fun isFitInBounds(linesCount: Int): Boolean {
        val totalWidth = textBounds.width()
        val totalHeight = linesCount * lineHeight()
        return totalWidth <= availableWidth && totalHeight <= availableHeight
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        availableWidth = measuredWidth - paddingStart - paddingRight
        availableHeight = measuredHeight - paddingTop - paddingBottom
    }
}