package host.capitalquiz.arondit.gameslist.domain

import java.util.Date

interface GameMapper<R> {
    operator fun invoke(id: Long, date: Date, playersInfo: List<PlayerInfo>): R
}
