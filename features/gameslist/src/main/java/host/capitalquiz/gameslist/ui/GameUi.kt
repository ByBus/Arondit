package host.capitalquiz.gameslist.ui

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip

data class GameUi(
    val id: Long,
    val dayMonth: String,
    val year: String,
    val playersColorWithInfo: List<Pair<Int, String>>,
) {
    fun update(playerViews: List<Chip>, dayMonth: TextView, year: TextView, infoBlock: Group) {
        dayMonth.text = this.dayMonth
        year.text = this.year
        infoBlock.isVisible = playersColorWithInfo.isEmpty()
        if (playersColorWithInfo.isNotEmpty()) {
            for ((i, info) in playersColorWithInfo.withIndex()) {
                playerViews[i].isVisible = true
                playerViews[i].chipBackgroundColor = ColorStateList.valueOf(info.first)
                playerViews[i].text = info.second
            }
        }
        for (i in playersColorWithInfo.size..playerViews.lastIndex) {
            playerViews[i].isVisible = false
        }
    }
}