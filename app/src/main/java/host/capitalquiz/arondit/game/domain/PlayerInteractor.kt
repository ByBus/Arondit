package host.capitalquiz.arondit.game.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

interface PlayerInteractor {

    fun allPlayersOfGame(gameId: Long): LiveData<List<Player>>

    suspend fun createPlayer(player: Player, gameId: Long): Long

    suspend fun deletePlayer(playerId: Long)

    class Base @Inject constructor(
        private val playerRepository: PlayerRepository,
    ): PlayerInteractor {
        override fun allPlayersOfGame(gameId: Long): LiveData<List<Player>> {
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