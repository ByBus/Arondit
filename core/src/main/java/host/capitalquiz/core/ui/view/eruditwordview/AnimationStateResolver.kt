package host.capitalquiz.core.ui.view.eruditwordview

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import androidx.core.animation.doOnEnd

class AnimationStateResolver(
    private val colorHolder: LetterColorHolder,
    private val colorAnimator: ValueAnimator?,
    private vararg val noBadgeColor: Int,
) : AnimationResolver {
    private var state: BadgeAnimationState = Forward(noBadgeColor[0])
    private var nextColor = noBadgeColor[0]

    override val color get() = state.color
    override val onEnd = { state.onEnd.invoke() }
    override val badge: Drawable? get() = state.badge
    override val showBadge: Boolean get() = state.showBadge
    override var charRevealing: Int = 0
        get() = state.charRevealing


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
            get() = nextColor !in noBadgeColor

        override val onEnd: () -> Unit = {
            val charReveal = findRevealAnimation(color, nextColor)
            state = (if (nextColor in noBadgeColor)
                Forward(nextColor).apply {
                    charRevealing = charReveal
                }
            else
                Reverse(nextColor))
        }

        override var charRevealing = IDLE

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

    private open inner class Reverse(override var color: Int) : BadgeAnimationState {
        override val showBadge: Boolean
            get() = color !in noBadgeColor

        override val onEnd: () -> Unit = {
            val charRevealing = findRevealAnimation(color, nextColor)
            state = Forward(color)
            state.charRevealing = charRevealing
            state.runAnimation()
        }

        override var charRevealing = IDLE

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

    private fun findRevealAnimation(colorFrom: Int, colorTo: Int): Int {
        return when {
            noBadgeColor.size < 2 || colorFrom == colorTo -> IDLE
            colorFrom == noBadgeColor[1] -> CHAR_HIDING
            colorTo == noBadgeColor[1] -> CHAR_REVEALING
            else -> IDLE
        }
    }

    companion object {
        const val CHAR_REVEALING = 1
        const val IDLE = 0
        const val CHAR_HIDING = -1
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

    var charRevealing: Int
}