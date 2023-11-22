package host.capitalquiz.core.ui.view.eruditwordview

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toRect
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlin.math.roundToInt

private const val CHAR_PLACEHOLDER = '*'

interface Letter {
    fun tryClick(event: MotionEvent): Boolean
    fun setBonus(bonus: Int, forceUpdate: Boolean)
    fun drawInside(bounds: RectF, canvas: Canvas)
    fun hasBonus(): Boolean
    fun invalidateCharWidth()

    class Base(
        character: Char,
        private var bonus: Int = 1,
        private val params: ParametersHolder,
        private var point: Int? = null
    ) : Letter {
        private val character = character.uppercaseChar()
        private var allowedChar = true
        val char get() = character.takeIf { allowedChar } ?: CHAR_PLACEHOLDER
        private var charWidth: Float = 0f
        private val bounds = RectF()

        private fun isAsterisk() = bonus == 0

        private var colorAnimator = ValueAnimator.ofInt(0, 0).apply {
            setEvaluator(ArgbEvaluator())
            duration = 300
            interpolator = FastOutSlowInInterpolator()

            addUpdateListener {
                params.invalidate()
            }
        }.takeIf {
            params.animateUpdates
        }

        private val animationState =
            AnimationStateResolver(
                params,
                colorAnimator,
                currentColor,
                params.letterBonusesColors[0]
            )
        private val currentColor: Int get() = params.letterBonusesColors[bonus]

        override fun setBonus(bonus: Int, forceUpdate: Boolean) {
            allowedChar = bonus >= 0
            if (allowedChar) this.bonus = bonus.coerceIn(0, 3)
            animationState.animateTo(currentColor)
            if (forceUpdate && params.badgeHeight == 0) params.requestLayout()
        }

        private fun draw(canvas: Canvas) {
            with(params) {
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

                val asteriskAlpha = (255f * (1f - animationState.animatedPosition())).roundToInt()

                val textAlpha =
                    if (animationState.charRevealing == AnimationStateResolver.CHAR_REVEALING)
                        255 - asteriskAlpha
                    else
                        255

                val drawAsterisk = isAsterisk() && animationState.showBadge.not()

                if (drawAsterisk) {
                    bounds.drawWithScale(0.65f) {
                        val asteriskBounds = this.toRect()
                        asteriskDrawable?.bounds = asteriskBounds
                        asteriskDrawable?.draw(canvas)
                        paint.withColor(
                            animationState.animatedColor().withAlpha(asteriskAlpha)
                        ) {
                            // impossible to animate the alpha of drawables because they are singletons
                            // so fill over them with transparent color
                            canvas.drawRect(asteriskBounds, this)
                        }
                    }
                } else {
                    val scoreTextSize = textSize / 2.5f
                    val score = (point ?: scoreOfChar(char)).toString()
                    var scoreXPosition = 0f
                    paint.withColor(textColor.withAlpha(textAlpha)) {
                        withTextSize(scoreTextSize) {
                            val scoreWidth = measureText(score)
                            val availableScoreWidth = blockSize / 3
                            val scale = if (availableScoreWidth > scoreWidth) 1f else availableScoreWidth/ scoreWidth
                            scoreXPosition = bounds.right - scoreWidth.coerceAtMost(availableScoreWidth) - offset
                            drawWithTextScaleX(scale){
                                canvas.drawText(score, scoreXPosition, yPosition + descent(), paint)
                            }
                        }
                        val availableSpace = scoreXPosition - xPosition
                        val scale =
                            if (availableSpace > charWidth) 1f else availableSpace / charWidth
                        drawWithTextScaleX(scale) {
                            canvas.drawText(char.toString(), xPosition, yPosition, paint)
                        }
                    }
                }
            }
        }

        override fun toString(): String = character.toString()

        override fun drawInside(bounds: RectF, canvas: Canvas) {
            this.bounds.set(bounds)
            draw(canvas)
        }

        override fun tryClick(event: MotionEvent): Boolean {
            return char != CHAR_PLACEHOLDER && bounds.contains(event.x, event.y)
        }

        override fun invalidateCharWidth() {
            charWidth = params.paint.measureText(char.toString())
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Base
            if (bonus == 0 && other.bonus == 0) return true
            if (character != other.character) return false
            return true
        }

        override fun hashCode(): Int = char.hashCode()

        override fun hasBonus(): Boolean = bonus > 1
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

private fun @receiver:ColorInt Int.withAlpha(alpha: Int): Int =
    ColorUtils.setAlphaComponent(this, alpha)