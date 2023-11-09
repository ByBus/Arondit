package host.capitalquiz.gameslist.data

import host.capitalquiz.core.db.GameData
import host.capitalquiz.core.db.GameDataMapper
import host.capitalquiz.core.db.PlayerDataMapper
import host.capitalquiz.core.db.PlayerWithWordsData
import host.capitalquiz.gameslist.domain.Game
import host.capitalquiz.gameslist.domain.PlayerInfo
import javax.inject.Inject

class GameDataToGameMapper @Inject constructor(private val mapper: PlayerDataMapper<PlayerInfo>) :
    GameDataMapper<Game> {
    override fun invoke(game: GameData, players: List<PlayerWithWordsData>): Game {
        val playersInfoDomain = players.map { it.map(mapper) }
        return Game(game.id, game.date, playersInfoDomain)
    }
}