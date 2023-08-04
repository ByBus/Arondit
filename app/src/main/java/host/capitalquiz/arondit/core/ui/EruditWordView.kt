package host.capitalquiz.arondit.core.ui

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
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.ColorUtils
import host.capitalquiz.arondit.R

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
    private val gap get() = blockSize * 0.05f
    private val word = mutableListOf<Letter>()
    private var blockSize = 0f
    private var radius = 0f
    private lateinit var multiplierRect: RectF
    private val bonus by lazy { Bonus() }
    private val x2Drawable by lazy {
        ContextCompat.getDrawable(
            context,
            R.drawable.ic_bonus_x2_strict
        )
    }
    private val x3Drawable by lazy {
        ContextCompat.getDrawable(
            context,
            R.drawable.ic_bonus_x3_strict
        )
    }
    var multiplier = 1
        set(value) {
            field = value
            requestLayout()
        }
    private val hasMultiplier get() = multiplier == 2 || multiplier == 3

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
        requestLayout()
    }

    fun setSize(@Px size: Int) {
        this.size = resources.displayMetrics.scaledDensity.toInt() * size
        requestLayout()
    }

    @SuppressLint("NewApi")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (word.isEmpty()) return

        if (hasMultiplier) {
            bonus.apply {
                text = multiplier.toString()
                bonusBgColor = if (multiplier == 2) WORD_X2_COLOR else WORD_X3_COLOR
            }.draw(canvas)
        }

        rect.apply {
            left = gap
            top = gap
            right = blockSize - gap
            bottom = blockSize - gap
        }
        word.forEach { letter ->
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
        if (hasMultiplier) {
            multiplierRect = RectF(0f, 0f, blockSize * word.size, height.toFloat())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth = size * word.size + if (hasMultiplier) size else 0
        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> Math.min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val desiredHeight = if (word.size == 0) 0 else Math.min(
            width / (word.size + if (hasMultiplier) 1 else 0),
            size
        )

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
                rect.with(top = rect.top + 1f) {
                    canvas.drawRoundRect(this, radius, radius, paint)
                }
            }
            rect.with(bottom = rect.bottom - rect.height() * 0.05f) {
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

    inner class Bonus(
        private val prefix: String = "✘",
        var text: String = "",
        var bonusBgColor: Int = LETTER_X2_COLOR,
    ) {
        @SuppressLint("NewApi")
        fun draw(canvas: Canvas) {
            val r = multiplierRect.height() / 2
            val halfSize = blockSize / 2
            paint.withColor(bonusBgColor) {
                multiplierRect.with(right = multiplierRect.right + halfSize) {
                    canvas.drawRoundRect(multiplierRect, radius, radius, paint)
                }
                multiplierRect.with(
                    left = multiplierRect.right - blockSize,
                    right = multiplierRect.right + blockSize
                ) {
                    canvas.drawRoundRect(multiplierRect, r, r, paint)
                }
            }
            val bonusDrawable = if (multiplier == 2) x2Drawable else x3Drawable
            if (bonusDrawable != null) {
                bonusDrawable.setBounds(
                    (multiplierRect.right).toInt(),
                    (multiplierRect.top).toInt(),
                    (multiplierRect.right + blockSize).toInt(),
                    (multiplierRect.bottom).toInt()
                )
                bonusDrawable.draw(canvas)
                return
            }
            paint.withColor(textColor) {
                withTextSize(paint.textSize * 0.65f) {
                    canvas.drawText(
                        prefix,
                        multiplierRect.right,
                        multiplierRect.height() * 0.65f,
                        paint
                    )
                }
                withTextSize(paint.textSize * 1.5f) {
                    val multTextSize = paint.textSize * 1.5f
                    canvas.drawText(
                        text,
                        multiplierRect.right + halfSize * 0.4f,
                        (multiplierRect.bottom - descent()) * 1.15f, paint
                    )
                }
            }
        }
    }


    companion object {
        private val scoresDictionary = mapOf(
            'А' to 1, 'Б' to 3, 'В' to 2, 'Г' to 3, 'Д' to 2, 'Е' to 1, 'Ё' to 1, 'Ж' to 5,
            'З' to 5, 'И' to 1, 'Й' to 2, 'К' to 2, 'Л' to 3, 'М' to 2, 'Н' to 1, 'О' to 1,
            'П' to 2, 'Р' to 2, 'С' to 2, 'Т' to 2, 'У' to 3, 'Ф' to 10, 'Х' to 5, 'Ц' to 10,
            'Ч' to 5, 'Ш' to 10, 'Щ' to 10, 'Ъ' to 10, 'Ы' to 5, 'Ь' to 5, 'Э' to 10, 'Ю' to 10,
            'Я' to 3, '*' to 0
        )
        private const val LETTER_X2_COLOR = (0XFF26A69A).toInt()
        private const val LETTER_X3_COLOR = (0XFBC02D).toInt()
        private const val WORD_X2_COLOR = (0XFF42A5F5).toInt()
        private const val WORD_X3_COLOR = (0XFFEF5350).toInt()
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