package host.capitalquiz.arondit.game.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import host.capitalquiz.arondit.core.db.PlayerDao
import host.capitalquiz.arondit.core.db.PlayerData
import host.capitalquiz.arondit.core.db.PlayerDataMapper
import host.capitalquiz.arondit.game.domain.Player
import host.capitalquiz.arondit.game.domain.PlayerRepository
import javax.inject.Inject

class BasePlayerRepository @Inject constructor(
    private val playerDao: PlayerDao,
    private val mapper: PlayerDataMapper<Player>,
) : PlayerRepository {
    override fun allPlayersOfGame(gameId: Long): LiveData<List<Player>> {
        return playerDao.allPlayerByGameId(gameId).map { players ->
            players.map { it.map(mapper) }
        }
    }

    override suspend fun createPlayer(player: Player, gameId: Long): Long {
        return playerDao.insert(PlayerData(player.name, player.color, gameId))
    }

    override suspend fun deletePlayer(playerId: Long) {
        playerDao.deletePlayerById(playerId)
    }
}