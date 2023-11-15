package host.capitalquiz.gameslist.domain

import java.util.Date

class Game(val id: Long, val date: Date, val playersInfo: List<PlayerInfo>, val ruleName: String) {
    fun <R> map(mapper: GameMapper<R>): R {
        return mapper(id, date, playersInfo, ruleName)
    }
}