package host.capitalquiz.arondit.onboarding


import android.graphics.PointF
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.CommandScheduler
import host.capitalquiz.arondit.core.ui.view.LottieCursorWrapper
import host.capitalquiz.arondit.databinding.FragmentHowToChangescoreBinding as ChangeScoreBinding


private const val POSITION_IN_VIEWPAGER = 2

class HowToChangeWordScoreFragment : BindingFragment<ChangeScoreBinding>() {

    override val viewInflater: Inflater<ChangeScoreBinding> = ChangeScoreBinding::inflate
    private val viewModel by viewModels<OnBoardingViewModel>(ownerProducer = { requireParentFragment() })
    private val scheduler = CommandScheduler()
    private val cursor by lazy { LottieCursorWrapper(binding.handCursor) }
    private val bonuses = mutableListOf<Int>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val word = getString(R.string.onboarding_change_score_example_word)
        bonuses.addAll(List(word.length) { 1 })

        binding.eruditWord.setText(word)

        viewModel.currentPage.observe(viewLifecycleOwner) {
            if (it == POSITION_IN_VIEWPAGER) launchAnimation()
        }

    }

    private fun incrementBonus(position: Int) {
        bonuses[position] = if (bonuses[position] == 3) 1 else bonuses[position] + 1
    }

    override fun onPause() {
        super.onPause()
        scheduler.cancel()
    }

    private fun launchAnimation() {
        val letterWidth = binding.eruditWord.width / bonuses.size
        scheduler.schedule {
            pause(100L)
            repeat(3)
            command {
                cursor.moveToAndShow { firstLetterPosition() }
            }
            command(100L) {
                cursor.click()
            }
            command(150L) {
                incrementBonus(0)
                binding.eruditWord.setBonuses(bonuses)
            }
            command(600L) {
                cursor.move(x = letterWidth * 2f)
            }
            command(200L) {
                cursor.click()
            }
            command(150L) {
                incrementBonus(2)
                binding.eruditWord.setBonuses(bonuses)
            }
            command(800L) {
                cursor.click()
            }
            command(150L) {
                incrementBonus(2)
                binding.eruditWord.setBonuses(bonuses)
            }
            command(600L) {
                cursor.move(x = letterWidth * 2f)
            }
            command(150L) {
                cursor.longClick()
            }
            command(400L) {
                bonuses[4] = if (bonuses[4] == 1) 0 else 1
                binding.eruditWord.setBonuses(bonuses)
            }
            command(500L) { cursor.hide() }
        }.execute()
    }

    private fun firstLetterPosition(): PointF {
        val letterWidth = binding.eruditWord.width / bonuses.size
        val x = binding.eruditWord.left + letterWidth / 2
        val y = binding.eruditWord.top + letterWidth / 2
        return PointF(x.toFloat(), y.toFloat())
    }
}