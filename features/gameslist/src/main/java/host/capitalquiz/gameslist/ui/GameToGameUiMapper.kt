package host.capitalquiz.gameslist.ui

import android.annotation.SuppressLint
import host.capitalquiz.gameslist.domain.GameMapper
import host.capitalquiz.gameslist.domain.PlayerInfo
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

private const val DELIMITER = "."
class GameToGameUiMapper @Inject constructor(): GameMapper<GameUi> {
    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("dd LLL${DELIMITER}yyyy")
    override fun invoke(id: Long, date: Date, playersInfo: List<PlayerInfo>, ruleName: String): GameUi {
        val players = playersInfo.sortedByDescending { it.score }.map { Pair(it.color, "${it.name} ${it.score}") }
        val dateList = simpleDateFormat.format(date).split(DELIMITER)
        return GameUi(id, dateList.first(), dateList.last(), players, ruleName)
    }
}