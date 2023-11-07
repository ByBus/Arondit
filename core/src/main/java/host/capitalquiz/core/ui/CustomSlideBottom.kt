package host.capitalquiz.core.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import androidx.transition.TransitionValues
import androidx.transition.Visibility


class CustomSlideBottom(private val durationMs: Long) : Visibility() {
    private var distance = 0f

    override fun onAppear(
        sceneRoot: ViewGroup?,
        view: View?,
        startValues: TransitionValues?,
        endValues: TransitionValues?,
    ): Animator {
        val offset = if (distance == 0f) (view?.height ?: 250).toFloat() else distance
        return animator(view, offset, 0f, OvershootInterpolator())
    }

    private fun animator(
        view: View?,
        startY: Float,
        endY: Float,
        animationInterpolator: Interpolator,
    ): Animator {
        return ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startY, endY).apply {
            interpolator = animationInterpolator
            duration = durationMs
        }
    }

    override fun onDisappear(
        sceneRoot: ViewGroup?,
        view: View?,
        startValues: TransitionValues?,
        endValues: TransitionValues?,
    ): Animator {
        val offset = if (distance == 0f) (view?.height ?: 250).toFloat() else distance
        return animator(view, 0f, offset, OvershootInterpolator())
    }
}