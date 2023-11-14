package host.capitalquiz.core.ui.view.eruditwordview


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.annotation.Px
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.DiffUtil
import host.capitalquiz.core.R
import kotlin.math.roundToInt


class EruditWordView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), Invalidator {
    private val params = Parameters(context, this)
    private var letterClickListener: LetterClickListener? = null
    private var letterLongClickListener: LetterLongClickListener? = null
    private var size = 0
    private val tempLetterBounds = RectF()
    private val word = mutableListOf<Letter>()
    private var clickableLetters = false
    private var diffUtil = false
    private var calculateSpaceForBadges = false

    init {
        context.withStyledAttributes(attrs, R.styleable.EruditWordView) {
            calculateSpaceForBadges = getBoolean(R.styleable.EruditWordView_initWithBadges, false)
            diffUtil = getBoolean(R.styleable.EruditWordView_useDiffUtil, false)
            params.animateUpdates = getBoolean(R.styleable.EruditWordView_animateUpdates, false)
            params.letterBonusesColors[0] =
                getColor(R.styleable.EruditWordView_asteriskColor, 0xFF607D8B.toInt())
            params.letterBonusesColors[1] =
                getColor(R.styleable.EruditWordView_mainColor, Color.GRAY).also {
                    params.paint.color = it
                }

            params.textColor = getColor(R.styleable.EruditWordView_textColor, Color.WHITE)
            clickableLetters =
                getBoolean(R.styleable.EruditWordView_clickableLetters, false)
            size = getDimensionPixelSize(R.styleable.EruditWordView_size, 100)
            updateWord(getString(R.styleable.EruditWordView_word), emptyList())
        }
    }

    private val wordBonus = WordBonus(params)

    private val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            val result = tryClick(e) { index ->
                letterClickListener?.onClick(index)
            }
            return if (result) true else super.onSingleTapConfirmed(e)
        }

        override fun onLongPress(e: MotionEvent) {
            tryClick(e) { index ->
                letterLongClickListener?.onLongClick(index)
            }
        }

        override fun onDown(e: MotionEvent): Boolean = true

        private fun tryClick(e: MotionEvent, clickListener: (Int) -> Unit): Boolean {
            word.forEachIndexed { index, letter ->
                if (letter.tryClick(e)) {
                    clickListener.invoke(index)
                    return true
                }
            }
            return false
        }
    }).takeIf { clickableLetters }

    /**
     * Update bonus of whole word
     *
     * value: [[1, 3]]
     */
    var multiplier = 1
        set(value) {
            val newValue = value.coerceIn(1, 3)
            if (field == newValue) return
            val bonusVisibilityChanged = (field == 1 || newValue == 1).not()
            field = newValue
            wordBonus.setBonus(newValue)
            hideAndShow(bonusVisibilityChanged) {
                if (!bonusVisibilityChanged) requestLayout()
            }
        }
    private val hasMultiplier get() = multiplier == 2 || multiplier == 3

    private fun hideAndShow(skipAnimation: Boolean = false, function: () -> Unit) {
        if (!params.animateUpdates || skipAnimation) {
            function.invoke()
            return
        }
        animate()
            .alpha(0f)
            .setDuration(150)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                function.invoke()
                show()
            }
            .start()
    }

    private fun show() {
        if (!params.animateUpdates) return
        animate()
            .alpha(1f)
            .setDuration(200)
            .setInterpolator(AccelerateInterpolator())
            .start()
    }

    private fun updateWord(string: String?, points: List<Int>) {
        if (string == null) return
        var newWord = string.mapIndexed<Letter> { i, char -> Letter.Base(char, params = params, point = points.getOrNull(i)) }
        if (diffUtil) {
            val wordCopy = word.toMutableList()
            val wordUpdater = WordUpdater(wordCopy, newWord)
            val diffUtil = wordUpdater.LetterDiffUtil()
            val diff: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtil)
            diff.dispatchUpdatesTo(wordUpdater)
            newWord = wordCopy
        }
        word.clear()
        word.addAll(newWord)
    }


    /**
     * Updates word chars
     */
    fun setText(text: String, points: List<Int> = emptyList()) {
        if (text.equals(toString(), true)) return
        hideAndShow {
            updateWord(text, points)
            requestLayout()
        }
    }

    fun setTextWithBonuses(text: String, bonuses: List<Int>, points: List<Int> = emptyList()) {
        if (text.equals(toString(), true)) {
            setBonuses(bonuses)
            return
        }
        hideAndShow {
            updateWord(text, points)
            setBonuses(bonuses)
            requestLayout()
        }
    }

    /**
     * Set size of letter block
     */
    fun setSize(@Px size: Int) {
        this.size = resources.displayMetrics.scaledDensity.toInt() * size
        requestLayout()
    }

    /**
     * Update bonuses of letter
     * @param letterPosition char position in word started from 0
     * @param bonus value of letter bonus<p> 1 = x1, 2 = x2, 3 = x3. Will be coerced in [[1, 3]] diapason
     */
    fun setBonus(letterPosition: Int, bonus: Int) {
        if (letterPosition < word.size) {
            word[letterPosition].setBonus(bonus, true)
        }
    }

    /**
     * Update bonuses of letter
     * @param values intArray of digits:
     *
     * 1 = x1, 2 = x2, 3 = x3.
     * Each value corresponds to a letter
     *
     * Values will be coerced in [[1, 3]] diapason
     */
    fun setBonuses(values: List<Int>) {
        params.badgeHeight = badgeHeight()
        for (i in 0..values.lastIndex) {
            setBonus(i, values[i])
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (word.isEmpty()) return

        wordBonus.draw(canvas)

        val gap = params.blockSize * GAP_FACTOR
        tempLetterBounds.set(
            gap, gap, params.blockSize - gap, params.blockSize - gap
        )
        word.forEach { letter ->
            letter.drawInside(tempLetterBounds, canvas)
            tempLetterBounds.offset(params.blockSize, 0f)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        with(params) {
            blockSize = height.toFloat() - badgeHeight
            radius = blockSize / 6f
            paint.textSize = blockSize * 0.7f
        }
        if (hasMultiplier) {
            updateWordBonusDrawBounds()
        }
        updateCharsWidths()
    }

    private fun updateWordBonusDrawBounds() {
        wordBonus.setBounds(0f, 0f, params.blockSize * word.size, params.blockSize)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val desiredWidth = size * word.size + if (hasMultiplier) size else 0
        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize // Must be this size
            MeasureSpec.AT_MOST -> Math.min(desiredWidth, widthSize) // Can't be bigger than
            else -> desiredWidth // Be whatever we want
        }
        params.badgeHeight = badgeHeight()
        val desiredHeight = if (word.size == 0) 0 else Math.min(
            (width / (word.size + if (hasMultiplier) 1 else 0)),
            size
        ) + params.badgeHeight

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(desiredHeight, heightSize)
            else -> desiredHeight
        }
        setMeasuredDimension(width, height)

        updateWordBonusDrawBounds()
        updateCharsWidths()
    }

    private fun badgeHeight() =
        (if (calculateSpaceForBadges || word.any { it.hasBonus() }) size * BADGE_HEIGHT_FACTOR else 0f).roundToInt()

    private fun updateCharsWidths() = word.forEach { it.invalidateCharWidth() }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector?.onTouchEvent(event) ?: super.onTouchEvent(event)
    }

    /**
     * Set clickListener to provide clicked letter index and its bonus value
     * @param listener callback for consuming clicked letter index and bonus
     */
    fun setLetterClickListener(listener: LetterClickListener) {
        letterClickListener = listener
    }

    /**
     * Set clickListener to provide long-clicked letter index
     * @param listener callback for consuming clicked letter index
     */
    fun setLetterLongClickListener(listener: LetterLongClickListener) {
        letterLongClickListener = listener
    }

    override fun onDetachedFromWindow() {
        letterClickListener = null
        letterLongClickListener = null
        super.onDetachedFromWindow()
    }


    override fun toString(): String {
        return word.joinToString("") { it.toString() }
    }

    fun interface LetterClickListener {
        fun onClick(index: Int)
    }

    fun interface LetterLongClickListener {
        fun onLongClick(index: Int)
    }

    companion object {
         /** Letter badge height as a fraction of 'size' parameter*/
        private const val BADGE_HEIGHT_FACTOR = 0.4f
        /** Spacing between letter blocks as a fraction of block size*/
        private const val GAP_FACTOR = 0.05f
    }
}

fun Paint.withColor(color: Int, block: Paint.() -> Unit) {
    val temp = this.color
    this.color = color
    block.invoke(this)
    this.color = temp
}

fun Paint.withTextSize(size: Float, block: Paint.() -> Unit) {
    val temp = textSize
    textSize = size
    block.invoke(this)
    textSize = temp
}

fun RectF.with(
    top: Float? = null,
    left: Float? = null,
    right: Float? = null,
    bottom: Float? = null,
    block: RectF.() -> Unit,
) {
    val leftTemp = this.left
    val topTemp = this.top
    val rightTemp = this.right
    val botTemp = this.bottom
    top?.let { this.top = it }
    left?.let { this.left = it }
    right?.let { this.right = it }
    bottom?.let { this.bottom = it }
    block.invoke(this)
    set(leftTemp, topTemp, rightTemp, botTemp)
}