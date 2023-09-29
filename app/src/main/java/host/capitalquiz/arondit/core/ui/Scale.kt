package host.capitalquiz.arondit.core.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.transition.TransitionValues
import androidx.transition.Visibility

private const val TRANSITION_SCALE = "host.capitalquiz.arondit:Scale:scale"

class Scale(private val durationShow: Long = 600L, private val durationHide: Long = 600L) : Visibility() {

    override fun onAppear(
        sceneRoot: ViewGroup?,
        view: View?,
        startValues: TransitionValues?,
        endValues: TransitionValues?,
    ): Animator {
        val scale = getStartScaleValue(startValues, TRANSITION_SCALE, 0f)

        return Animator(
            view = view,
            startScaleX = if (scale == 1f) 0f else scale,
            startScaleY = if (scale == 1f) 0f else scale,
            endScaleX = 1f,
            endScaleY = 1f,
            scaleInterpolator = OvershootInterpolator(),
            durationMs = durationShow
        )
    }

    override fun onDisappear(
        sceneRoot: ViewGroup?,
        view: View?,
        startValues: TransitionValues?,
        endValues: TransitionValues?,
    ): Animator {
        val scale = getStartScaleValue(startValues, TRANSITION_SCALE, 1f)

        return Animator(
            view = view,
            startScaleX = scale,
            startScaleY = scale,
            endScaleX = 0f,
            endScaleY = 0f,
            scaleInterpolator = DecelerateInterpolator(),
            durationMs = durationHide
        )
    }

    private fun Animator(
        view: View?,
        startScaleX: Float,
        startScaleY: Float,
        endScaleX: Float,
        endScaleY: Float,
        scaleInterpolator: Interpolator,
        durationMs: Long
    ): Animator {
        val animScaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, startScaleX, endScaleX)
        val animScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, startScaleY, endScaleY)

        val animator = AnimatorSet().apply {
            interpolator = scaleInterpolator
            duration = durationMs
            playTogether(animScaleX, animScaleY)
            doOnEnd {
                view?.scaleX = 1f
                view?.scaleY = 1f
            }
        }

        return animator
    }

    private fun getStartScaleValue(
        scaleValue: TransitionValues?,
        property: String,
        default: Float,
    ): Float {
        return scaleValue?.values?.get(property) as? Float ?: default
    }
}