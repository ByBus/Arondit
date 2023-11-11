package host.capitalquiz.gameslist.data

import host.capitalquiz.core.db.GameData
import host.capitalquiz.core.db.GameDataMapper
import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.PlayerDataMapperWithParameter
import host.capitalquiz.core.db.PlayerWithWordsData
import host.capitalquiz.gameslist.domain.Game
import host.capitalquiz.gameslist.domain.PlayerInfo
import javax.inject.Inject

class GameDataToGameMapper @Inject constructor(
    private val mapper: PlayerDataMapperWithParameter<GameRuleData, PlayerInfo>
) : GameDataMapper<Game> {
    override fun invoke(game: GameData, players: List<PlayerWithWordsData>, gameRule: GameRuleData): Game {
        val playersInfoDomain = players.map { mapper.map(it, gameRule) }
        return Game(game.id, game.date, playersInfoDomain)
    }
}