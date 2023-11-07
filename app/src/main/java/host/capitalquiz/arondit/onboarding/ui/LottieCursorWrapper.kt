package host.capitalquiz.arondit.onboarding.ui

import android.graphics.PointF
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.RawRes
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import host.capitalquiz.arondit.R
import kotlin.math.pow
import kotlin.math.sqrt


class LottieCursorWrapper(private val cursor: LottieAnimationView) {
    private val currentTranslation = PointF()
    private val parabola = ParabolaInterpolator()
    private val quarterArch = CircleArchInterpolator()

    fun move(x: Float, y: Float = 0f, duration: Long = 500L) {
        cursor
            .animate()
            .setInterpolator(DecelerateInterpolator())
            .setUpdateListener {
                val value = it.animatedValue as Float
                cursor.translationX = currentTranslation.x + x * value
                cursor.translationY = currentTranslation.y +
                        if (y == 0f)
                            x * -0.6f * parabola.getInterpolation(value)
                        else
                            y * value
            }
            .setDuration(duration)
            .withEndAction {
                currentTranslation.set(cursor.translationX, cursor.translationY)
            }.start()
    }

    fun click() {
        cursor.clickAnimation()
    }

    fun longClick() {
        cursor.clickAnimation(true)
    }

    fun moveToAndShow(
        coordinatesProducer: () -> PointF,
        duration: Long = 400L,
    ) {
        val (x, y) = coordinatesProducer.invoke()
        currentTranslation.set(x - (cursor.left + cursor.width * 0.5f), y - cursor.top)
        cursor.animate()
            .alpha(0.8f)
            .setInterpolator(FastOutSlowInInterpolator())
            .setUpdateListener {
                val value = it.animatedValue as Float
                cursor.translationX = currentTranslation.x *
                        (quarterArch.getInterpolation(value - 1f))
                cursor.translationY = currentTranslation.y * value
            }
            .setDuration(duration)
            .withEndAction {
                currentTranslation.set(cursor.translationX, cursor.translationY)
            }.start()
    }

    fun hide(duration: Long = 400L) {
        cursor.animate()
            .setDuration(duration)
            .setInterpolator(AccelerateInterpolator())
            .alpha(0f)
            .setUpdateListener {
                val value = it.animatedValue as Float
                cursor.translationX = currentTranslation.x - currentTranslation.x * (1f - quarterArch.getInterpolation(value))
                cursor.translationY = currentTranslation.y - currentTranslation.y * value
            }
    }

    private fun LottieAnimationView.clickAnimation(isLongClick: Boolean = false) {
        setLottieAnimation(R.raw.press)
        postDelayed({
            setLottieAnimation(R.raw.unpress)
        }, if (isLongClick) 500L else 100L)
    }

    private fun LottieAnimationView.setLottieAnimation(@RawRes animation: Int) {
        val task = LottieCompositionFactory.fromRawRes(this.context, animation)
        task.addListener { composition ->
            setComposition(composition)
            playAnimation()
        }
    }
}

class ParabolaInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float = -(input - 0.5f).pow(2) + 0.25f
}

class CircleArchInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float = sqrt(1 - input * input)
}