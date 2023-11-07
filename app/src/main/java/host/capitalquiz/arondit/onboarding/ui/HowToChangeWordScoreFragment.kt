package host.capitalquiz.arondit.onboarding.ui


import android.graphics.PointF
import android.os.Bundle
import android.view.View
import host.capitalquiz.core.ui.CommandScheduler
import host.capitalquiz.core.R
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.arondit.databinding.FragmentHowToChangescoreBinding as ChangeScoreBinding


class HowToChangeWordScoreFragment : BaseOnBoardingFragment<ChangeScoreBinding>() {

    override val positionInViewPager = 2
    override val viewInflater: Inflater<ChangeScoreBinding> = ChangeScoreBinding::inflate
    private var cursor: LottieCursorWrapper? = null
    private val bonuses = mutableListOf<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cursor = LottieCursorWrapper(binding.handCursor)
        val word = getString(R.string.onboarding_change_score_example_word)
        bonuses.addAll(List(word.length) { 1 })
        binding.eruditWord.setText(word)
    }

    private fun incrementBonus(position: Int) {
        bonuses[position] = if (bonuses[position] == 3) 1 else bonuses[position] + 1
    }

    override fun onPause() {
        super.onPause()
        bonuses.replaceAll { 1 }
        binding.eruditWord.setBonuses(bonuses)
        cursor?.hide(0L)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cursor = null
    }

    override fun CommandScheduler.animationSchedule() {
        val letterWidth = binding.eruditWord.width.toFloat() / bonuses.size
        pause(100L)
        repeatBelow(3)
        command(500L) { cursor?.moveToAndShow(::firstLetterCenterPosition) }
        command(300L) { cursor?.click() }
        command(200L) {
            incrementBonus(0)
            binding.eruditWord.setBonuses(bonuses)
        }
        command(600L) { cursor?.move(x = letterWidth * 2) }
        command(300L) { cursor?.click() }
        command(200L) {
            incrementBonus(2)
            binding.eruditWord.setBonuses(bonuses)
        }
        command(800L) { cursor?.click() }
        command(200L) {
            incrementBonus(2)
            binding.eruditWord.setBonuses(bonuses)
        }
        command(600L) { cursor?.move(x = letterWidth * 2) }
        command(300L) { cursor?.longClick() }
        command(400L) {
            bonuses[4] = if (bonuses[4] == 1) 0 else 1
            binding.eruditWord.setBonuses(bonuses)
        }
        command(500L) { cursor?.hide() }
    }

    private fun firstLetterCenterPosition(): PointF {
        val letterWidth = binding.eruditWord.width / bonuses.size
        val x = binding.eruditWord.left + letterWidth / 2
        val y = binding.eruditWord.top + letterWidth / 2
        return PointF(x.toFloat(), y.toFloat())
    }
}