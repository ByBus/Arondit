package host.capitalquiz.arondit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Px
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.ColorUtils

class EruditWordView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private var color = 0
    private val thicknessColor = (0x70000000).toInt()
    private var textColor = 0
    private var size = 0
    private val paint = Paint()
        .apply {
            strokeWidth = 2f
            isAntiAlias = true
        }
    private val rect = RectF()
    private val gap = 3f
    private val word = mutableListOf<Letter>()
    private var blockSize = 0f
    private var radius = 0f
    var multiplier = 1
        set(value) {
            field = value
            invalidate()
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.EruditWordView) {
            color = getColor(R.styleable.EruditWordView_mainColor, Color.GRAY).also {
                paint.color = it
            }
            textColor = getColor(R.styleable.EruditWordView_textColor, Color.WHITE)
            size = getDimensionPixelSize(R.styleable.EruditWordView_size, 100)
            updateWord(getString(R.styleable.EruditWordView_word))
        }
    }

    private fun updateWord(string: String?) {
        string?.let {
            word.clear()
            it.forEach { letter ->
                val char = letter.uppercaseChar()
                    .takeIf { c -> scoresDictionary.containsKey(c) } ?: '*'
                word.add(Letter(char))
            }
        }
    }

    fun setText(text: String) {
        updateWord(text)
        invalidate()
    }

    fun setSize(@Px size: Int) {
        this.size = resources.displayMetrics.scaledDensity.toInt() * size
        invalidate()
    }

    @SuppressLint("NewApi")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (word.isEmpty()) {
            return
        }
        rect.apply {
            left = gap
            top = gap
            right = blockSize - gap
            bottom = blockSize - gap
        }
        for (letter in word) {
            letter.draw(canvas)
            rect.offset(blockSize, 0f)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        blockSize = height.toFloat()
        radius = blockSize / 6f
        paint.textSize = blockSize * 0.7f
        val text = word.joinToString("") { it.toString() }
        val widths = FloatArray(word.size).also {
            paint.getTextWidths(text, 0, it.size, it)
        }
        for (i in widths.indices) {
            word[i].width = widths[i]
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth = size * word.size
        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> Math.min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val desiredHeight = Math.min(width / word.size, size)

        //Measure Height
        //1.Must be this size
        //2.Can't be bigger than...
        //3.Be whatever you want
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(desiredHeight, heightSize)
            else -> desiredHeight
        }
        setMeasuredDimension(width, height)
    }

    inner class Letter(private val char: Char, var width: Float = 10f, var bonus: Int = 1) {
        fun draw(canvas: Canvas) {
            paint.withColor(ColorUtils.compositeColors(thicknessColor, color)) {
                rect.with(top = rect.top + 1f, bottom = rect.bottom + rect.height() * 0.05f) {
                    canvas.drawRoundRect(this, radius, radius, paint)
                }
            }
            rect.with(bottom = rect.bottom - 2f) {
                canvas.drawRoundRect(this, radius, radius, paint)
            }

            val offset = (blockSize - width) / 5
            val xPosition = rect.left + offset
            val textSize = paint.descent() - paint.ascent()
            val yPosition =
                rect.bottom - (blockSize - textSize) / 2 - paint.descent()

            paint.withColor(textColor) {
                val score = scoresDictionary[char]
                val scoreTextSize = textSize / 2.5f
                var scoreXPosition = 0f
                withTextSize(scoreTextSize) {
                    val scoreString = score.toString()
                    val scoreWidth = measureText(scoreString)
                    scoreXPosition = rect.right - scoreWidth - offset
                    canvas.drawText(
                        scoreString,
                        scoreXPosition,
                        yPosition + descent(),
                        paint
                    )
                }
                val availableSpace = scoreXPosition - xPosition
                val scale = if (availableSpace > width) 1f else availableSpace / width
                drawWithTextScaleX(scale) {
                    canvas.drawText(char.toString(), xPosition, yPosition, paint)
                }
            }
        }

        override fun toString(): String = char.toString()
    }


    companion object {
        private val scoresDictionary = mapOf(
            'А' to 1, 'Б' to 3, 'В' to 2, 'Г' to 3, 'Д' to 2, 'Е' to 1, 'Ё' to 1, 'Ж' to 5,
            'З' to 5, 'И' to 1, 'Й' to 2, 'К' to 2, 'Л' to 3, 'М' to 2, 'Н' to 1, 'О' to 1,
            'П' to 2, 'Р' to 2, 'С' to 2, 'Т' to 2, 'У' to 3, 'Ф' to 10, 'Х' to 5, 'Ц' to 10,
            'Ч' to 5, 'Ш' to 10, 'Щ' to 10, 'Ъ' to 10, 'Ы' to 5, 'Ь' to 5, 'Э' to 10, 'Ю' to 10,
            'Я' to 3, '*' to 0
        )
    }
}

private fun Paint.withColor(color: Int, block: Paint.() -> Unit) {
    val temp = this.color
    this.color = color
    block.invoke(this)
    this.color = temp
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

private fun Rect.update(
    top: Int? = null,
    left: Int? = null,
    right: Int? = null,
    bottom: Int? = null,
) {
    top?.let { this.top = it }
    left?.let { this.left = it }
    right?.let { this.right = it }
    bottom?.let { this.bottom = it }
}

private fun RectF.with(
    top: Float = Float.NEGATIVE_INFINITY,
    left: Float = Float.NEGATIVE_INFINITY,
    right: Float = Float.NEGATIVE_INFINITY,
    bottom: Float = Float.NEGATIVE_INFINITY,
    block: RectF.() -> Unit,
) {
    val topTemp = this.top
    val leftTemp = this.left
    val rightTemp = this.right
    val botTemp = this.bottom
    if (top != Float.NEGATIVE_INFINITY) this.top = top
    if (left != Float.NEGATIVE_INFINITY) this.left = left
    if (right != Float.NEGATIVE_INFINITY) this.right = right
    if (bottom != Float.NEGATIVE_INFINITY) this.bottom = bottom
    block.invoke(this)
    this.left = leftTemp
    this.right = rightTemp
    this.top = topTemp
    this.bottom = botTemp
}