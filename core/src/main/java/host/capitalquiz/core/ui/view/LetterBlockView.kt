package host.capitalquiz.core.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import host.capitalquiz.core.R

class LetterBlockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private var char = 'A'
    private var points = 1
    private var background: Drawable? = null
    private var desiredSize = 0
    private val blockBounds = Rect()
    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private var charWidth = 1f
    private val textXferMode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)

    init {
        context.withStyledAttributes(attrs, R.styleable.LetterBlockView) {
            char = getString(R.styleable.LetterBlockView_letter)?.first() ?: 'A'
            points = getInt(R.styleable.LetterBlockView_points, 1)
            background = getDrawable(R.styleable.LetterBlockView_blockBackground)
            paint.color = getColor(R.styleable.LetterBlockView_letterColor, 0xFF3C3C3C.toInt())
            desiredSize = getDimensionPixelSize(R.styleable.LetterBlockView_blockSize, 50)
        }
    }

    fun randomize() {
        updateLetter(('А'..'Я').random(), (0..10).random())
    }

    fun updateLetter(char: Char, points: Int) {
        this.char = char
        this.points = points
        charWidth = paint.measureText(char.toString())
        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        background?.draw(canvas)

        val scoreTextSize = paint.textSize / 2.5f
        val offset = (blockBounds.width() - charWidth) / 3.5f
        val xPosition = blockBounds.left + offset
        val yPosition =
            blockBounds.bottom - (blockBounds.width() - paint.textSize) / 2 - paint.descent()
        var scoreXPosition = 0f
        val xfermodeTemp = paint.xfermode
        paint.xfermode = textXferMode
        paint.withTextSize(scoreTextSize) {
            val score = points.toString()
            val scoreWidth = measureText(score)
            scoreXPosition = blockBounds.right - scoreWidth - offset
            canvas.drawText(
                score,
                scoreXPosition,
                yPosition + descent(),
                paint
            )
        }
        val availableSpace = scoreXPosition - xPosition
        val scale =
            if (availableSpace > charWidth) 1f else availableSpace / charWidth
        paint.drawWithTextScaleX(scale) {
            canvas.drawText(char.toString(), xPosition, yPosition, paint)
        }
        paint.xfermode = xfermodeTemp
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth = desiredSize + paddingLeft + paddingRight

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize // Must be this size
            MeasureSpec.AT_MOST -> Math.min(desiredWidth, widthSize) // Can't be bigger than
            else -> desiredWidth // Be whatever we want
        }
        val desiredHeight = desiredSize + paddingTop + paddingBottom

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(desiredHeight, heightSize)
            else -> desiredHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        blockBounds.set( /*l t r b*/
            paddingLeft,
            paddingTop,
            paddingLeft + (w - paddingLeft - paddingRight),
            paddingTop + (h - paddingTop - paddingBottom)
        )
        paint.textSize = blockBounds.height() * 0.7f
        charWidth = paint.measureText(char.toString())
        background?.bounds = blockBounds
    }

    private fun Paint.withTextSize(size: Float, block: Paint.() -> Unit) {
        val temp = textSize
        textSize = size
        block.invoke(this)
        textSize = temp
    }

    private fun Paint.drawWithTextScaleX(widthScale: Float, block: Paint.() -> Unit) {
        if (textScaleX == widthScale) {
            block.invoke(this)
            return
        }
        val temp = this.textScaleX
        textScaleX = widthScale
        block.invoke(this)
        textScaleX = temp
    }
}