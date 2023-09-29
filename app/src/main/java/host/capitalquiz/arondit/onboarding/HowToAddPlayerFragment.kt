package host.capitalquiz.arondit.onboarding

import android.graphics.PointF
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.CommandScheduler
import host.capitalquiz.arondit.core.ui.CustomSlideBottom
import host.capitalquiz.arondit.core.ui.view.LottieCursorWrapper
import host.capitalquiz.arondit.databinding.FragmentHowToAddPlayerBinding as AddPlayerBinding

private const val POSITION_IN_VIEWPAGER = 0

class HowToAddPlayerFragment : BindingFragment<AddPlayerBinding>() {

    override val viewInflater: Inflater<AddPlayerBinding> = AddPlayerBinding::inflate
    private val viewModel by viewModels<OnBoardingViewModel>(ownerProducer = { requireParentFragment() })
    private val scheduler = CommandScheduler()
    private val cursor by lazy { LottieCursorWrapper(binding.handCursor) }
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

        with(binding) {
            playerHeader1.setScore(42)
            playerHeader1.setName(getString(R.string.onboarding_add_player_name1))
            playerHeader2.setScore(0)
            playerHeader2.setName(getString(R.string.onboarding_add_player_name2))
            playerHeader2.setColor(ContextCompat.getColor(requireActivity(), R.color.base_blue))
        }

        viewModel.currentPage.observe(viewLifecycleOwner) {
            if (it == POSITION_IN_VIEWPAGER) launchAnimation()
        }
    }

    override fun onPause() {
        super.onPause()
        scheduler.cancel()
        binding.playerHeader2.isVisible = false
    }

    private fun launchAnimation() {
        scheduler.schedule {
            pause(100L)
            repeat(3)
            command { cursor.moveToAndShow { buttonAddOffsetFromCursor() } }
            command(300L) { cursor.click() }
            command(100L) {
                binding.playerHeader1.addPlayerButton().startAnimation(clickAnimation)
            }
            command(200L) {
                TransitionManager.beginDelayedTransition(binding.root, transition)
                binding.playerHeader2.isVisible = true
            }
            command(800L) { cursor.move(x = 580f) }
            command(300L) { cursor.click() }
            command(100L) {
                binding.playerHeader2.removePlayerButton().startAnimation(clickAnimation)
            }
            command(100L) {
                TransitionManager.beginDelayedTransition(binding.root, transition)
                binding.playerHeader2.isVisible = false
            }
            command(800L) { cursor.hide() }
        }.execute()
    }

    private fun buttonAddOffsetFromCursor(): PointF {
        val addPlayerButton = binding.playerHeader1.addPlayerButton()
        val x = binding.playerHeader1.left + addPlayerButton.left + addPlayerButton.width / 2 + 10
        val y = binding.playerHeader1.top + addPlayerButton.top + addPlayerButton.height / 2
        return PointF(x.toFloat(), y.toFloat())
    }
}