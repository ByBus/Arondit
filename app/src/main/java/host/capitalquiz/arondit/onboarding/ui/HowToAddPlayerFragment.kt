package host.capitalquiz.arondit.onboarding.ui

import android.graphics.PointF
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.CommandScheduler
import host.capitalquiz.arondit.core.ui.CustomSlideBottom
import host.capitalquiz.arondit.core.ui.Inflater
import host.capitalquiz.arondit.core.ui.view.LottieCursorWrapper
import host.capitalquiz.arondit.databinding.FragmentHowToAddPlayerBinding as AddPlayerBinding


class HowToAddPlayerFragment : BaseOnBoardingFragment<AddPlayerBinding>() {

    override val positionInViewPager = 0
    override val viewInflater: Inflater<AddPlayerBinding> = AddPlayerBinding::inflate
    private var cursor: LottieCursorWrapper? = null
    private val transition by lazy {
        (TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.onboarding_add_player_header) as TransitionSet)
            .addTransition(CustomSlideBottom(500L))
    }

    private val clickAnimation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.click_and_scale)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cursor = LottieCursorWrapper(binding.handCursor)

        with(binding) {
            playerHeader1.setScore(42)
            playerHeader1.setName(getString(R.string.onboarding_add_player_name1))
            playerHeader2.setScore(0)
            playerHeader2.setName(getString(R.string.onboarding_add_player_name2))
            playerHeader2.setColor(ContextCompat.getColor(requireActivity(), R.color.base_blue))
        }
    }

    override fun onPause() {
        super.onPause()
        cursor?.hide(0L)
        binding.playerHeader2.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cursor = null
    }

    override fun CommandScheduler.animationSchedule() {
        var distanceBetweenButtons = 0f
        pause(100L)
        command { distanceBetweenButtons = distanceBetweenAddAndRemoveButtons() }
        repeatBelow(3)
        command(400L) { cursor?.moveToAndShow(::addButtonCenterPosition) }
        command(400L) { cursor?.click() }
        command(100L) {
            binding.playerHeader1.addPlayerButton().startAnimation(clickAnimation)
        }
        command(200L) {
            TransitionManager.beginDelayedTransition(binding.root, transition)
            binding.playerHeader2.isVisible = true
        }
        command(800L) { cursor?.move(x = distanceBetweenButtons) }
        command(500L) { cursor?.click() }
        command(100L) {
            binding.playerHeader2.removePlayerButton().startAnimation(clickAnimation)
        }
        command(100L) {
            TransitionManager.beginDelayedTransition(binding.root, transition)
            binding.playerHeader2.isVisible = false
        }
        command(800L) { cursor?.hide() }
    }

    private fun distanceBetweenAddAndRemoveButtons(): Float {
        return with(binding.playerHeader1) {
            (removePlayerButton().left - addPlayerButton( ).left).toFloat()
        }
    }

    private fun addButtonCenterPosition(): PointF {
        val addPlayerButton = binding.playerHeader1.addPlayerButton()
        val x = binding.playerHeader1.left + addPlayerButton.left + addPlayerButton.width / 2 + 10
        val y = binding.playerHeader1.top + addPlayerButton.top + addPlayerButton.height / 2
        return PointF(x.toFloat(), y.toFloat())
    }
}