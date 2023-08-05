package host.capitalquiz.arondit.gameslist.data

import host.capitalquiz.arondit.core.db.GameData
import host.capitalquiz.arondit.core.db.GameDataMapper
import host.capitalquiz.arondit.core.db.PlayerDataMapper
import host.capitalquiz.arondit.core.db.PlayerWithWordsData
import host.capitalquiz.arondit.gameslist.domain.Game
import host.capitalquiz.arondit.gameslist.domain.PlayerInfo
import javax.inject.Inject

class GameDataToGameMapper @Inject constructor(private val mapper: PlayerDataMapper<PlayerInfo>) :
    GameDataMapper<Game> {
    override fun invoke(game: GameData, players: List<PlayerWithWordsData>): Game {
        val playersInfoDomain = players.map { it.map(mapper) }
        return Game(game.id, game.date, playersInfoDomain)
    }
}