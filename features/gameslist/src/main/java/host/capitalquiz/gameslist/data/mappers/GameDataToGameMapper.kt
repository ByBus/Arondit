package host.capitalquiz.gameslist.data.mappers

import host.capitalquiz.core.db.GameData
import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.PlayerWithWordsData
import host.capitalquiz.core.db.mappers.GameDataMapper
import host.capitalquiz.core.db.mappers.PlayerDataMapperWithParameter
import host.capitalquiz.gameslist.domain.Game
import host.capitalquiz.gameslist.domain.PlayerInfo
import javax.inject.Inject

class GameDataToGameMapper @Inject constructor(
    private val mapper: PlayerDataMapperWithParameter<GameRuleData, PlayerInfo>
) : GameDataMapper<Game> {
    override fun invoke(game: GameData, players: List<PlayerWithWordsData>, gameRule: GameRuleData): Game {
        val playersInfoDomain = players.map { mapper.map(it, gameRule) }
        return Game(game.id, game.date, playersInfoDomain, gameRule.name)
    }
}