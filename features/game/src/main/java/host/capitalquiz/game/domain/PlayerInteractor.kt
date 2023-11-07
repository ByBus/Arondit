package host.capitalquiz.game.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

interface PlayerInteractor {

    fun findAllPlayersOfGame(gameId: Long): LiveData<List<Player>>

    suspend fun createPlayer(player: Player, gameId: Long): Long

    suspend fun deletePlayer(playerId: Long)

    class Base @Inject constructor(
        private val playerRepository: PlayerRepository,
    ): PlayerInteractor {
        override fun findAllPlayersOfGame(gameId: Long): LiveData<List<Player>> {
            return playerRepository.allPlayersOfGame(gameId)
        }

        override suspend fun createPlayer(player: Player, gameId: Long): Long {
            return playerRepository.createPlayer(player, gameId)
        }

        override suspend fun deletePlayer(playerId: Long) {
            playerRepository.deletePlayer(playerId)
        }
    }
}