package host.capitalquiz.game

import host.capitalquiz.core.db.PlayerDao
import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.PlayerRepository
import javax.inject.Inject

class BasePlayerRepository @Inject constructor(
    private val playerDao: PlayerDao,
) : PlayerRepository {
    override suspend fun allPlayers(): List<Player> {
        return playerDao.allPlayers().map {
            Player(it.id, it.name)
        }
    }

    override suspend fun createPlayerWithName(name: String): Long {
        return playerDao.upsert(PlayerData(name))
    }

    override suspend fun renamePlayer(name: String, playerId: Long) {
        playerDao.upsert(PlayerData(name).apply { id = playerId })
    }

    override suspend fun deletePlayer(playerId: Long) {
        playerDao.deletePlayer(playerId)
    }
}