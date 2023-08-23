package host.capitalquiz.arondit.core.ui.view.eruditwordview

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toRect
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlin.math.roundToInt

interface Letter {
    fun tryClick(event: MotionEvent, bonusConsumer: ((Int) -> Unit)?): Boolean
    fun tryLongClick(event: MotionEvent): Boolean
    fun setBonus(bonus: Int, forceUpdate: Boolean)
    fun drawInside(bounds: RectF, canvas: Canvas)
    fun hasBonus(): Boolean
    fun invalidateCharWidth()

    class Base(
        character: Char,
        private var bonus: Int = 1,
        private val params: ParametersHolder,
    ) : Letter {
        val char =
            character.uppercaseChar().takeIf { params.isAllowedChar(it) }
                ?: '*'
        private var charWidth: Float = 0f
        private val bounds = RectF()
        private var isFirstDraw = true

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

        private fun nextBonus() {
            setBonus(bonus++ % params.letterBonusesColors.size, true)
        }

        override fun setBonus(bonus: Int, forceUpdate: Boolean) {
            this.bonus = bonus.coerceIn(0, 3)
            animationState.animateTo(currentColor)
            if (forceUpdate && params.badgeHeight == 0) params.requestLayout()
        }

        fun draw(canvas: Canvas) {
            val blockColor = animationState.animatedColor()
            if (animationState.showBadge) {
                params.paint.withColor(blockColor) {
                    val smallRadius = params.radius / 3
                    val animationPosition = animationState.animatedPosition()
                    val currentPos = params.badgeHeight * animationPosition
                    val top = bounds.bottom - params.badgeHeight + currentPos
                    val left = bounds.left + smallRadius
                    val right = bounds.right - smallRadius
                    val bottom = bounds.bottom + currentPos
                    bounds.with(top, left, right, bottom) {
                        canvas.drawRoundRect(this, smallRadius, smallRadius, params.paint)
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

            with(params) {
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
                    val score = params.scoreOfChar(char)
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
                            if (animateUpdates && isFirstDraw.not()) {
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
                        val scale =
                            if (availableSpace > charWidth) 1f else availableSpace / charWidth
                        paint.withColor(ColorUtils.setAlphaComponent(textColor, textAlpha)) {
                            drawWithTextScaleX(scale) {
                                canvas.drawText(char.toString(), xPosition, yPosition, paint)
                            }
                        }
                    }
                }
            }
        }

        override fun toString(): String = char.toString()

        override fun drawInside(bounds: RectF, canvas: Canvas) {
            this.bounds.set(bounds)
            draw(canvas)
        }

        override fun tryClick(event: MotionEvent, bonusConsumer: ((Int) -> Unit)?): Boolean {
            if (char != '*' && bounds.contains(event.x, event.y)) {
                isFirstDraw = false
                bonusConsumer?.invoke(bonus) ?: nextBonus()
                return true
            }
            return false
        }

        override fun tryLongClick(event: MotionEvent): Boolean {
            if (char != '*' && bounds.contains(event.x, event.y)) {
                isFirstDraw = false
                return true
            }
            return false
        }

        override fun invalidateCharWidth() {
            charWidth = params.paint.measureText(char.toString())
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Base
            if (bonus == 0 && other.bonus == 0) return true
            if (char != other.char) return false
            return true
        }

        override fun hashCode(): Int = char.hashCode() + bonus

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