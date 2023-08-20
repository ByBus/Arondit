package host.capitalquiz.arondit.game.ui

import android.os.Build
import android.widget.TextView
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import host.capitalquiz.arondit.core.ui.view.EruditWordView

data class WordUi(
    val word: String = "",
    val letterBonuses: List<Int> = emptyList(),
    val multiplier: Int = 1,
    val id: Long = 0,
    val score: Int = 0,
) {
    fun <R> map(mapper: WordUiMapper<R>): R {
        return mapper(word, letterBonuses, multiplier, id, score)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun update(
        wordView: EruditWordView,
        x2Button: ToggleButton,
        x3Button: ToggleButton,
    ) {
        x2Button.isChecked = multiplier == 2
        x3Button.isChecked = multiplier == 3
        update(wordView)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun update(wordView: EruditWordView) {
        wordView.setTextWithBonuses(word, letterBonuses)
        wordView.multiplier = multiplier
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun update(eruditWord: EruditWordView, wordScores: TextView) {
        update(eruditWord)
        wordScores.text = score.toString()
    }
}
