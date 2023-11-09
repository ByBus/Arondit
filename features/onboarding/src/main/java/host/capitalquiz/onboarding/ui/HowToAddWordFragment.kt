package host.capitalquiz.onboarding.ui

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
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import host.capitalquiz.onboarding.R
import host.capitalquiz.core.R as RCore
import host.capitalquiz.core.ui.CommandScheduler
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.core.ui.RoundedRectangleSpan
import host.capitalquiz.core.ui.Scale
import host.capitalquiz.core.ui.view.CompositeBorderDrawable
import host.capitalquiz.onboarding.databinding.FragmentHowToAddWordBinding as AddWordBinding


class HowToAddWordFragment : BaseOnBoardingFragment<AddWordBinding>() {

    override val positionInViewPager = 1
    override val viewInflater: Inflater<AddWordBinding> = AddWordBinding::inflate
    private var cursor: LottieCursorWrapper? = null
    private val transition by lazy {
        (TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.onboarding_add_word) as TransitionSet)
            .addTransition(Scale(durationHide = 300L))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cursor = LottieCursorWrapper(binding.handCursor)

        binding.inputWordWindow.background = CompositeBorderDrawable(
            requireContext(),
            leftTopCorner = RCore.drawable.dialog_border_top_left_corner,
            leftVerticalPipe = RCore.drawable.dialog_border_vertical_pipe,
            leftBottomCorner = RCore.drawable.dialog_border_bottom_left_corner,
            bottomHorizontalPipe = RCore.drawable.dialog_border_horizontal_pipe,
            topHorizontalPipe = RCore.drawable.dialog_border_top_hor_pipe,
            topHorizontalDecorTile = RCore.drawable.dialog_border_top_hor_pipe_pattern
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

    }

    override fun onPause() {
        super.onPause()
        binding.addWord.isVisible = true
        binding.inputWordWindow.isVisible = false
        cursor?.hide(0L)
        binding.typeWriter.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cursor = null
    }

    override fun CommandScheduler.animationSchedule() {
        val wordsToType = resources.getStringArray(R.array.onboarding_input_words)
        pause(200L)
        repeatBelow(3)
        command { cursor?.moveToAndShow(::addWordButtonCenterPosition) }
        command(300L) { cursor?.click() }
        command(100L) { binding.addWord.isPressed = true }
        command(200L) {
            TransitionManager.beginDelayedTransition(binding.root, transition)
            binding.addWord.isPressed = false
            binding.addWord.isVisible = false
            binding.inputWordWindow.isVisible = true
        }
        command { cursor?.hide() }
        command { iteration ->
            binding.typeWriter.type(wordsToType[iteration], 300L)
        }
        command(3500L) {
            TransitionManager.beginDelayedTransition(binding.root, transition)
            binding.addWord.isVisible = true
            binding.inputWordWindow.isVisible = false
        }
        command(1000L) { binding.typeWriter.setText("") }
    }

    private fun addWordButtonCenterPosition(): PointF {
        val addButton = binding.addWord
        val x = addButton.left + addButton.width / 2
        val y = addButton.top + addButton.height / 2
        return PointF(x.toFloat(), y.toFloat())
    }
}