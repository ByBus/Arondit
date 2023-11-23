package host.capitalquiz.game.data

import host.capitalquiz.core.db.PlayerDao
import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.mappers.PlayerDataMapperWithParameter
import host.capitalquiz.game.domain.GameRuleRepository
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BasePlayerRepository @Inject constructor(
    private val playerDao: PlayerDao,
    private val mapper: PlayerDataMapperWithParameter<GameRuleSimple, Player>,
    private val gameRuleRepository: GameRuleRepository
) : PlayerRepository {

    override fun allPlayersOfGame(gameId: Long): Flow<List<Player>> {
        return playerDao.allPlayerByGameId(gameId).map { players ->
            val rule = gameRuleRepository.gameRuleOfGame(gameId)
            players.map { mapper.map(it, rule) }
        }
    }

    override suspend fun createPlayer(player: Player, gameId: Long): Long {
        return playerDao.insert(PlayerData(player.name, player.color, gameId))
    }

    override suspend fun deletePlayer(playerId: Long) {
        playerDao.deletePlayerById(playerId)
    }
}