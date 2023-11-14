package host.capitalquiz.game.ui

import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.view.isVisible
import host.capitalquiz.core.ui.view.eruditwordview.EruditWordView

data class WordUi(
    val word: String = "",
    val letterBonuses: List<Int> = emptyList(),
    val multiplier: Int = 1,
    val id: Long = 0,
    val score: Int = 0,
    val extraPoints: Int,
    val points: List<Int> = emptyList()
) {
    fun <R> map(mapper: WordUiMapper<R>): R {
        return mapper(word, letterBonuses, multiplier, id, score, extraPoints)
    }

    fun update(
        wordView: EruditWordView,
        x2Button: ToggleButton,
        x3Button: ToggleButton,
        extraPointButton: ToggleButton
    ) {
        x2Button.isChecked = multiplier == 2
        x3Button.isChecked = multiplier == 3
        extraPointButton.isVisible = extraPoints > 0 || word.length > 7
        extraPointButton.isChecked = extraPoints > 0
        update(wordView)
    }

    private fun update(wordView: EruditWordView) {
        wordView.setTextWithBonuses(word, letterBonuses, points)
        wordView.multiplier = multiplier
    }

    fun update(eruditWord: EruditWordView, wordScores: TextView, extraPoints: TextView) {
        update(eruditWord)
        wordScores.text = score.toString()
        extraPoints.isVisible = this.extraPoints > 0
    }
}
