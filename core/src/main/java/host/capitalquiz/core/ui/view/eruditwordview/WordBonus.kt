package host.capitalquiz.core.ui.view.eruditwordview

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.RectF
import android.view.animation.AccelerateDecelerateInterpolator

class WordBonus(private val params: ParametersHolder) {
    private var wordBounds: RectF = RectF()

    private val badgeAnimator = ValueAnimator.ofInt(0, 0).apply {
        setEvaluator(ArgbEvaluator())
        duration = 250
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            params.invalidate()
        }
    }.takeIf { params.animateUpdates }

    private val bonusAnimationState =
        AnimationStateResolver(
            BonusColorAdapter(params, params.x1Color),
            badgeAnimator,
            params.x1Color
        )

    fun setBonus(value: Int) {
        bonusAnimationState.animateTo(
            when (value) {
                2 -> params.x2Color
                3 -> params.x3Color
                else -> params.x1Color
            }
        )
    }

    fun setBounds(left: Float, top: Float, right: Float, bottom: Float) {
        wordBounds.set(left, top, right, bottom)
    }

    fun draw(canvas: Canvas) {
        with(params) {
            if (bonusAnimationState.showBadge.not()) return
            val offset = bonusAnimationState.animatedPosition() * blockSize
            val r = (wordBounds.height() / 3 * bonusAnimationState.animatedPosition())
                .coerceAtLeast(radius)
            paint.withColor(bonusAnimationState.animatedColor()) {
                wordBounds.with(right = wordBounds.right - r + offset) {
                    canvas.drawRoundRect(wordBounds, radius, radius, paint)
                }
                wordBounds.with(
                    left = wordBounds.right - blockSize + offset,
                    right = wordBounds.right + offset
                ) {
                    canvas.drawRoundRect(wordBounds, r, r, paint)
                }
            }
            bonusAnimationState.badge?.let { bonusDrawable ->
                bonusDrawable.setBounds(
                    (wordBounds.right - blockSize + offset).toInt(),
                    (wordBounds.top).toInt(),
                    (wordBounds.right + offset).toInt(),
                    (wordBounds.bottom).toInt()
                )
                bonusDrawable.draw(canvas)
            }
        }
    }
}