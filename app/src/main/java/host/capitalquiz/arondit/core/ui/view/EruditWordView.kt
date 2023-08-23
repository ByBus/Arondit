package host.capitalquiz.arondit.core.ui.view


import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
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
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toRect
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.DiffUtil
import host.capitalquiz.arondit.R
import kotlin.math.roundToInt


class EruditWordView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), ColorHolder {
    private var letterClickListener: LetterClickListener? = null
    private var letterLongClickListener: LetterLongClickListener? = null

    // index = bonus: 0 is for asterisk color, 1 is for base color
    private val letterBonusesColors = intArrayOf(0, 0, LETTER_X2_COLOR, LETTER_X3_COLOR)
    private val thicknessColor = (0x70000000).toInt()
    private var textColor = 0
    private var size = 0
    private val paint = Paint()
        .apply {
            strokeWidth = 2f
            isAntiAlias = true
        }
    private val tempLetterBounds = RectF()
    private val gap get() = blockSize * 0.05f
    private val word = mutableListOf<Letter>()
    private var blockSize = 0f
    private var radius = 0f
    private var multiplierRect: RectF = RectF()
    private val wordBonus by lazy { WordBonus() }
    override val x2Drawable by lazy { R.drawable.ic_bonus_x2_strict.loadDrawable(context) }
    override val x3Drawable by lazy { R.drawable.ic_bonus_x3_strict.loadDrawable(context) }
    override val x2LetterDrawable by lazy { R.drawable.letter_x2_bonus.loadDrawable(context) }
    override val x3LetterDrawable by lazy { R.drawable.letter_x3_bonus.loadDrawable(context) }
    private val asteriskDrawable by lazy { R.drawable.ic_asterisk6_curved_24.loadDrawable(context) }

    override val baseColor: Int get() = letterBonusesColors[1]

    override val x2LetterColor: Int get() = letterBonusesColors[2]

    override val x3LetterColor: Int get() = letterBonusesColors[3]

    override val x2Color = WORD_X2_COLOR
    override val x3Color = WORD_X3_COLOR

    private var clickableLetters = false
    private var diffUtil = false
    private var animateUpdates = false
    private var calculateSpaceForBadges = false

    init {
        context.withStyledAttributes(attrs, R.styleable.EruditWordView) {
            calculateSpaceForBadges = getBoolean(R.styleable.EruditWordView_initWithBadges, false)
            diffUtil = getBoolean(R.styleable.EruditWordView_useDiffUtil, false)
            animateUpdates = getBoolean(R.styleable.EruditWordView_animateUpdates, false)
            letterBonusesColors[0] = getColor(R.styleable.EruditWordView_asteriskColor, 0xFF607D8B.toInt())
            letterBonusesColors[1] = getColor(R.styleable.EruditWordView_mainColor, Color.GRAY).also {
                paint.color = it
            }

            textColor = getColor(R.styleable.EruditWordView_textColor, Color.WHITE)
            clickableLetters =
                getBoolean(R.styleable.EruditWordView_clickableLetters, false)
            size = getDimensionPixelSize(R.styleable.EruditWordView_size, 100)
            updateWord(getString(R.styleable.EruditWordView_word))
        }
    }

    private var badgeHeight = 0

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
                    2 -> x2Color
                    3 -> x3Color
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
    }.takeIf { animateUpdates }

    private val bonusAnimationState =
        AnimationStateResolver(
            BonusColorAdapter(this@EruditWordView, WORD_X1_COLOR),
            multiplierBadgeAnimator,
            WORD_X1_COLOR,
        )


    private fun hideAndShow(skipAnimation: Boolean = false, function: () -> Unit) {
        if (!animateUpdates || skipAnimation) {
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
        if (!animateUpdates) return
        animate()
            .alpha(1f)
            .setDuration(200)
            .setInterpolator(AccelerateInterpolator())
            .start()
    }


    private fun updateWord(string: String?) {
        if (string == null) return
        val newWord = string.map { Letter(it) }
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
        badgeHeight = badgeHeight()
        for (i in 0..values.lastIndex) {
            setBonus(i, values[i])
        }
    }

    @SuppressLint("NewApi")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (word.isEmpty()) return

        wordBonus.apply {
            text = multiplier.toString()
        }.draw(canvas)

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
        blockSize = height.toFloat() - badgeHeight
        radius = blockSize / 6f
        paint.textSize = blockSize * 0.7f
        if (hasMultiplier) {
            updateMultipliyerDrawBounds()
        }
        updateCharsWidths()
    }

    private fun updateMultipliyerDrawBounds() {
        multiplierRect = RectF(0f, 0f, blockSize * word.size, blockSize)
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
        badgeHeight = badgeHeight()
        val desiredHeight = if (word.size == 0) 0 else Math.min(
            (width / (word.size + if (hasMultiplier) 1 else 0)),
            size
        ) + badgeHeight

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

    inner class Letter(private val character: Char, private var bonus: Int = 1) {
        val char = character.uppercaseChar().takeIf { scoresDictionary.containsKey(it) } ?: '*'
        private var charWidth: Float = 0f
        private val bounds = RectF()
        private var isFirstShow = true

        private fun isAsterisk() = bonus == 0

        private var colorAnimator = ValueAnimator.ofInt(0, 0).apply {
            setEvaluator(ArgbEvaluator())
            duration = 300
            interpolator = FastOutSlowInInterpolator()

            addUpdateListener {
                invalidate()
            }
        }.takeIf {
            animateUpdates
        }

        private val animationState =
            AnimationStateResolver(
                this@EruditWordView,
                colorAnimator,
                currentColor,
                letterBonusesColors[0]
            )
        private val currentColor: Int get() = letterBonusesColors[bonus]

        fun nextBonus() {
            setBonus(bonus++ % letterBonusesColors.size)
        }

        fun setBonus(value: Int, forceUpdate: Boolean = true) {
            bonus = value.coerceIn(0, 3)
            animationState.animateTo(currentColor)
            if (forceUpdate && badgeHeight == 0) requestLayout()
        }

        fun draw(canvas: Canvas) {
            val blockColor = animationState.animatedColor()
            if (animationState.showBadge) {
                paint.withColor(blockColor) {
                    val smallRadius = radius / 3
                    val animationPosition = animationState.animatedPosition()
                    val currentPos = badgeHeight * animationPosition
                    val top = bounds.bottom - badgeHeight + currentPos
                    val left = bounds.left + smallRadius
                    val right = bounds.right - smallRadius
                    val bottom = bounds.bottom + currentPos
                    bounds.with(top, left, right, bottom) {
                        canvas.drawRoundRect(this, smallRadius, smallRadius, paint)
                    }
                    animationState.badge?.let {
                        it.setBounds(
                            left.toInt(),
                            top.toInt(),
                            right.toInt(),
                            bottom.toInt()
                        )
                        it.draw(canvas)
                    }
                }
            }

            paint.withColor(ColorUtils.compositeColors(thicknessColor, blockColor)) {
                bounds.with(top = bounds.top + 1f) {
                    canvas.drawRoundRect(this, radius, radius, paint)
                }
            }

            paint.withColor(blockColor) {
                val bottomOffset = bounds.height() * 0.05f
                bounds.with(bottom = bounds.bottom - bottomOffset) {
                    canvas.drawRoundRect(this, radius, radius, paint)
                }
            }

            val offset = (bounds.width() - charWidth) / 5
            val xPosition = bounds.left + offset
            val textSize = paint.descent() - paint.ascent()
            val yPosition =
                bounds.bottom - (bounds.width() - textSize) / 2 - paint.descent()

            val asteriskAlpha =
                if (animationState.charRevealing == AnimationStateResolver.IDLE)
                    255
                else
                    (255 * (1 - animationState.animatedPosition())).roundToInt()

            val textAlpha =
                if (animationState.charRevealing == AnimationStateResolver.CHAR_REVEALING)
                    255 - asteriskAlpha
                else
                    255

            paint.withColor(ColorUtils.setAlphaComponent(textColor, textAlpha)) {
                val score = scoresDictionary[char]
                val scoreTextSize = textSize / 2.5f
                var scoreXPosition = 0f
                withTextSize(scoreTextSize) {
                    val scoreString = score.toString()
                    val scoreWidth = measureText(scoreString)
                    scoreXPosition = bounds.right - scoreWidth - offset
                    if (!(isAsterisk() && animationState.showBadge.not())) {
                        canvas.drawText(
                            scoreString,
                            scoreXPosition,
                            yPosition + descent(),
                            paint
                        )
                    }
                }

                if (isAsterisk() && animationState.showBadge.not()) {
                    bounds.drawWithScale(0.65f) {
                        val asteriskBounds = this.toRect()
                        asteriskDrawable?.bounds = asteriskBounds
                        asteriskDrawable?.draw(canvas)
                        if (animateUpdates && isFirstShow.not()) {
                            paint.withColor(
                                ColorUtils.setAlphaComponent(
                                    animationState.animatedColor(),
                                    asteriskAlpha
                                )
                            ) {
                                // impossible to animate the alpha of images because they are singletons
                                // so fill over them with transparent color
                                canvas.drawRect(asteriskBounds, this)
                            }
                        }
                    }
                } else {
                    val availableSpace = scoreXPosition - xPosition
                    val scale = if (availableSpace > charWidth) 1f else availableSpace / charWidth
                    paint.withColor(ColorUtils.setAlphaComponent(textColor, textAlpha)) {
                        drawWithTextScaleX(scale) {
                            canvas.drawText(char.toString(), xPosition, yPosition, paint)
                        }
                    }
                }
            }
        }

        override fun toString(): String = char.toString()

        fun drawInside(newBounds: RectF, canvas: Canvas) {
            bounds.set(newBounds)
            draw(canvas)
        }

        fun tryClick(event: MotionEvent, bonusConsumer: ((Int) -> Unit)?): Boolean {
            if (char != '*' && bounds.contains(event.x, event.y)) {
                isFirstShow = false
                bonusConsumer?.invoke(bonus) ?: nextBonus()
                return true
            }
            return false
        }

        fun tryLongClick(event: MotionEvent): Boolean {
            if (char != '*' && bounds.contains(event.x, event.y)) {
                isFirstShow = false
                return true
            }
            return false
        }

        fun invalidateCharWidth() {
            charWidth = paint.measureText(char.toString())
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Letter
            if (bonus == 0 && other.bonus == 0) return true
            if (char != other.char) return false
            return true
        }

        override fun hashCode(): Int = char.hashCode() + bonus

        fun hasBonus(): Boolean = bonus > 1
    }

    inner class WordBonus(
        private val prefix: String = "✘",
        var text: String = "",
    ) {
        @SuppressLint("NewApi")
        fun draw(canvas: Canvas) {
            if (bonusAnimationState.showBadge.not()) return
            val halfSize = blockSize / 2
            val offset = bonusAnimationState.animatedPosition() * blockSize
            val r =
                (multiplierRect.height() / 3 * bonusAnimationState.animatedPosition()).coerceAtLeast(
                    radius
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
                    canvas.drawText(
                        text,
                        multiplierRect.right + halfSize * 0.4f,
                        (multiplierRect.bottom - descent()) * 1.15f, paint
                    )
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

    fun interface LetterClickListener {
        fun onClick(index: Int, bonus: Int)
    }

    fun interface LetterLongClickListener {
        fun onLongClick(index: Int)
    }

    override fun toString(): String {
        return word.joinToString("") { it.toString() }
    }

    companion object {
        private const val BADGE_HEIGHT_OF_BLOCK_HEIGHT = 2.5f
        private val scoresDictionary = mapOf(
            'А' to 1, 'Б' to 3, 'В' to 2, 'Г' to 3, 'Д' to 2, 'Е' to 1, 'Ё' to 1, 'Ж' to 5,
            'З' to 5, 'И' to 1, 'Й' to 2, 'К' to 2, 'Л' to 3, 'М' to 2, 'Н' to 1, 'О' to 1,
            'П' to 2, 'Р' to 2, 'С' to 2, 'Т' to 2, 'У' to 3, 'Ф' to 10, 'Х' to 5, 'Ц' to 10,
            'Ч' to 5, 'Ш' to 10, 'Щ' to 10, 'Ъ' to 10, 'Ы' to 5, 'Ь' to 5, 'Э' to 10, 'Ю' to 10,
            'Я' to 3, '*' to 0
        )
        private const val LETTER_X2_COLOR = 0xFF26A69A.toInt()
        private const val LETTER_X3_COLOR = 0xFFFBC02D.toInt()
        private const val WORD_X2_COLOR = 0xFF42A5F5.toInt()
        private const val WORD_X3_COLOR = 0xFFEF5350.toInt()
        private const val WORD_X1_COLOR = Color.TRANSPARENT
    }
}

private fun RectF.drawWithScale(scale: Float, function: RectF.() -> Unit?) {
    val offsetHor = (width() - width() * scale) * 0.5f
    val offsetVert = (height() - height() * scale) * 0.5f
    with(
        top = top + offsetVert,
        left = left + offsetHor,
        right = right - offsetHor,
        bottom = bottom - offsetVert
    ) {
        function.invoke(this)
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

private fun @receiver:DrawableRes Int.loadDrawable(context: Context) =
    ContextCompat.getDrawable(context, this)