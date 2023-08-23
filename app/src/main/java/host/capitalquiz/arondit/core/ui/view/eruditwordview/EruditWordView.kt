package host.capitalquiz.arondit.core.ui.view.eruditwordview


import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
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
import host.capitalquiz.arondit.R
import kotlin.math.roundToInt


class EruditWordView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), Invalidator {
    private val params = Parameters(context, this)
    private var letterClickListener: LetterClickListener? = null
    private var letterLongClickListener: LetterLongClickListener? = null
    private var size = 0
    private val tempLetterBounds = RectF()
    private val gap get() = blockSize * 0.05f
    private val word = mutableListOf<Letter>()
    private var blockSize = 0f
    private var multiplierRect: RectF = RectF()
    private val wordBonus by lazy { WordBonus() }
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
            updateWord(getString(R.styleable.EruditWordView_word))
        }
    }

    private val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            word.forEachIndexed { index, letter ->
                val bonusConsumer = letterClickListener?.let {
                    { bonus: Int -> it.onClick(index, bonus) }
                }
                if (letter.tryClick(e, bonusConsumer)) return true
            }
            return super.onSingleTapConfirmed(e)
        }

        override fun onLongPress(e: MotionEvent) {
            word.forEachIndexed { index, letter ->
                if (letter.tryLongClick(e)) {
                    letterLongClickListener?.onLongClick(index)
                }
            }
        }

        override fun onDown(e: MotionEvent): Boolean = true
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
            bonusAnimationState.animateTo(
                when (newValue) {
                    2 -> params.x2Color
                    3 -> params.x3Color
                    else -> WORD_X1_COLOR
                }
            )
            hideAndShow(bonusVisibilityChanged) {
                if (!bonusVisibilityChanged) requestLayout()
            }
        }
    private val hasMultiplier get() = multiplier == 2 || multiplier == 3

    private val multiplierBadgeAnimator = ValueAnimator.ofInt(0, 0).apply {
        setEvaluator(ArgbEvaluator())
        duration = 250
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            invalidate()
        }
    }.takeIf { params.animateUpdates }

    private val bonusAnimationState =
        AnimationStateResolver(
            BonusColorAdapter(params, WORD_X1_COLOR),
            multiplierBadgeAnimator,
            WORD_X1_COLOR,
        )


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

    private fun updateWord(string: String?) {
        if (string == null) return
        val newWord = string.map { Letter.Base(it, params = params) }
        if (diffUtil) {
            val wordUpdater = WordUpdater(word, newWord)
            val diffUtil = wordUpdater.LetterDiffUtil()
            val diff: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtil)
            diff.dispatchUpdatesTo(wordUpdater)
        } else {
            word.clear()
            word.addAll(newWord)
        }
    }


    /**
     * Updates word chars
     */
    fun setText(text: String) {
        if (text.equals(toString(), true)) return
        hideAndShow {
            updateWord(text)
            requestLayout()
        }
    }

    fun setTextWithBonuses(text: String, bonuses: List<Int>) {
        if (text.equals(toString(), true)) {
            setBonuses(bonuses)
            return
        }
        hideAndShow {
            updateWord(text)
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

        tempLetterBounds.apply {
            left = gap
            top = gap
            right = blockSize - gap
            bottom = blockSize - gap
        }
        word.forEach { letter ->
            letter.drawInside(tempLetterBounds, canvas)
            tempLetterBounds.offset(blockSize, 0f)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        blockSize = height.toFloat() - params.badgeHeight
        params.radius = blockSize / 6f
        params.paint.textSize = blockSize * 0.7f
        if (hasMultiplier) {
            updateMultipliyerDrawBounds()
        }
        updateCharsWidths()
    }

    private fun updateMultipliyerDrawBounds() {
        multiplierRect.set(0f, 0f, blockSize * word.size, blockSize)
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

        updateMultipliyerDrawBounds()
        updateCharsWidths()
    }

    private fun badgeHeight() =
        (if (calculateSpaceForBadges || word.any { it.hasBonus() }) size / BADGE_HEIGHT_OF_BLOCK_HEIGHT else 0f).roundToInt()

    private fun updateCharsWidths() = word.forEach { it.invalidateCharWidth() }

    inner class WordBonus {
        fun draw(canvas: Canvas) {
            with(params) {
                if (bonusAnimationState.showBadge.not()) return
                val offset = bonusAnimationState.animatedPosition() * blockSize
                val r =
                    (multiplierRect.height() / 3 * bonusAnimationState.animatedPosition()).coerceAtLeast(
                        params.radius
                    )
                paint.withColor(bonusAnimationState.animatedColor()) {
                    multiplierRect.with(right = multiplierRect.right - r + offset) {
                        canvas.drawRoundRect(multiplierRect, radius, radius, paint)
                    }
                    multiplierRect.with(
                        left = multiplierRect.right - blockSize + offset,
                        right = multiplierRect.right + offset
                    ) {
                        canvas.drawRoundRect(multiplierRect, r, r, paint)
                    }
                }
                bonusAnimationState.badge?.let { bonusDrawable ->
                    bonusDrawable.setBounds(
                        (multiplierRect.right - blockSize + offset).toInt(),
                        (multiplierRect.top).toInt(),
                        (multiplierRect.right + offset).toInt(),
                        (multiplierRect.bottom).toInt()
                    )
                    bonusDrawable.draw(canvas)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (gestureDetector != null) {
            return gestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
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
        fun onClick(index: Int, bonus: Int)
    }

    fun interface LetterLongClickListener {
        fun onLongClick(index: Int)
    }

    companion object {
        private const val BADGE_HEIGHT_OF_BLOCK_HEIGHT = 2.5f
        private const val WORD_X1_COLOR = Color.TRANSPARENT
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