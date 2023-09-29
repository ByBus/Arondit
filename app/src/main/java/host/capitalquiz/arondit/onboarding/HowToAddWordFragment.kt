package host.capitalquiz.arondit.onboarding

import android.graphics.Color
import android.graphics.PointF
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.TypedValue
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.CommandScheduler
import host.capitalquiz.arondit.core.ui.RoundedRectangleSpan
import host.capitalquiz.arondit.core.ui.Scale
import host.capitalquiz.arondit.core.ui.view.CompositeBorderDrawable
import host.capitalquiz.arondit.core.ui.view.LottieCursorWrapper
import host.capitalquiz.arondit.databinding.FragmentHowToAddWordBinding as AddWordBinding

private const val POSITION_IN_VIEWPAGER = 1

class HowToAddWordFragment : BindingFragment<AddWordBinding>() {

    override val viewInflater: Inflater<AddWordBinding> = AddWordBinding::inflate
    private val viewModel by viewModels<OnBoardingViewModel>(ownerProducer = { requireParentFragment() })
    private val scheduler = CommandScheduler()
    private val cursor by lazy { LottieCursorWrapper(binding.handCursor) }
    private val transition by lazy {
        (TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.onboarding_add_word) as TransitionSet)
            .addTransition(Scale(durationHide = 300L))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputWordWindow.background = CompositeBorderDrawable(
            requireContext(),
            leftTopCorner = R.drawable.dialog_border_top_left_corner,
            leftVerticalPipe = R.drawable.dialog_border_vertical_pipe,
            leftBottomCorner = R.drawable.dialog_border_bottom_left_corner,
            bottomHorizontalPipe = R.drawable.dialog_border_horizontal_pipe,
            topHorizontalPipe = R.drawable.dialog_border_top_hor_pipe,
            topHorizontalDecorTile = R.drawable.dialog_border_top_hor_pipe_pattern
        ).apply {
            moveDecorSides(-15, 15)
        }

        val text = getString(R.string.onboarding_add_word_hint)
        val spannableString = SpannableString(text)
        val start = text.indexOf(getString(R.string.onboarding_add_word_highlighted))

        val typedValue = TypedValue()
        requireActivity().theme.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
        val density = resources.displayMetrics.density
        val roundedRectangleSpan =
            RoundedRectangleSpan(
                typedValue.data,
                Color.WHITE,
                8 * density,
                Typeface.DEFAULT,
                18 * density
            )


        spannableString.setSpan(
            roundedRectangleSpan,
            start,
            start + 5,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.infoText.text = spannableString

        binding.typeWriter.addTextChangedListener {
            val string = it.toString()
            val notBlank = string.isNotBlank()
            binding.eruditWord.isVisible = notBlank
            TransitionManager.beginDelayedTransition(binding.inputWordWindow)
            if (notBlank) binding.eruditWord.setText(string)
        }

        viewModel.currentPage.observe(viewLifecycleOwner) {
            if (it == POSITION_IN_VIEWPAGER) launchAnimation()
        }

    }

    override fun onPause() {
        super.onPause()
        binding.addWord.isVisible = true
        binding.inputWordWindow.isVisible = false
        scheduler.cancel()
        binding.typeWriter.stop()
    }

    private fun launchAnimation() {
        val wordsToType = resources.getStringArray(R.array.onboarding_input_words)
        scheduler.schedule {
            pause(200L)
            repeat(3)
            command { cursor.moveToAndShow { buttonCoordinates() } }
            command(100L) { binding.addWord.isPressed = true }
            command { cursor.click() }
            command(100L) {
                TransitionManager.beginDelayedTransition(binding.root, transition)
                binding.addWord.isPressed = false
                binding.addWord.isVisible = false
                binding.inputWordWindow.isVisible = true
            }
            command { cursor.hide() }
            command { iteration ->
                binding.typeWriter.type(wordsToType[iteration], 300L)
            }
            command(3500L) {
                TransitionManager.beginDelayedTransition(binding.root, transition)
                binding.addWord.isVisible = true
                binding.inputWordWindow.isVisible = false
            }
            command(1000L) { binding.typeWriter.setText("") }
        }.execute()
    }

    private fun buttonCoordinates(): PointF {
        val addButton = binding.addWord
        val x = addButton.left + addButton.width / 2
        val y = addButton.top + addButton.height / 2
        return PointF(x.toFloat(), y.toFloat())
    }
}