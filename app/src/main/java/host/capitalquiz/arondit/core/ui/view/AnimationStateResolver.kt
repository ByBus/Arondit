package host.capitalquiz.arondit.core.ui.view

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import androidx.core.animation.doOnEnd

class AnimationStateResolver(
    private val colorHolder: LetterColorHolder,
    initColor: Int,
    private val colorAnimator: ValueAnimator?,
) : AnimationResolver {
    private var state: BadgeAnimationState = Forward(initColor)
    private var nextColor = initColor

    override val color get() = state.color
    override val onEnd = { state.onEnd.invoke() }
    override val badge: Drawable? get() = state.badge
    override val showBadge: Boolean get() = state.showBadge

    init {
        colorAnimator?.doOnEnd {
            onEnd.invoke()
        }
    }

    override fun runAnimation() = state.runAnimation()

    override fun animatedColor(): Int {
        return if (colorAnimator?.isRunning == true)
            colorAnimator.animatedValue as Int
        else
            state.color
    }

    override fun animatedPosition(): Float = colorAnimator?.animatedFraction ?: 1f

    private inner class Forward(override var color: Int) : BadgeAnimationState {
        override val showBadge: Boolean
            get() = nextColor != colorHolder.baseColor

        override val onEnd: () -> Unit = {
            state = if (nextColor == colorHolder.baseColor)
                Forward(nextColor)
            else
                Reverse(nextColor)
        }

        override val badge: Drawable?
            get() = if (nextColor == colorHolder.x2LetterColor)
                colorHolder.x2LetterDrawable
            else
                colorHolder.x3LetterDrawable

        override fun runAnimation() {
            colorAnimator?.let {
                it.setIntValues(color, nextColor)
                it.start()
            } ?: onEnd()
        }
    }

    private inner class Reverse(override var color: Int) : BadgeAnimationState {
        override val showBadge: Boolean
            get() = color != colorHolder.baseColor

        override val onEnd: () -> Unit = {
            state = Forward(color)
            state.runAnimation()
        }

        override val badge: Drawable?
            get() = if (color == colorHolder.x2LetterColor)
                colorHolder.x2LetterDrawable
            else
                colorHolder.x3LetterDrawable

        override fun runAnimation() {
            colorAnimator?.let {
                it.setIntValues(color, color)
                it.reverse()
            } ?: onEnd()
        }
    }

    override fun animateTo(value: Int) {
        if (value == state.color) return
        nextColor = value
        runAnimation()
    }
}

interface AnimationResolver : BadgeAnimationState {
    fun animateTo(value: Int)
    fun animatedColor(): Int
    fun animatedPosition(): Float
}

interface BadgeAnimationState {
    val color: Int
    val onEnd: () -> Unit
    val badge: Drawable?
    fun runAnimation()
    val showBadge: Boolean
}