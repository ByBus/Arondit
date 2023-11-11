package host.capitalquiz.game.domain

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun allPlayersOfGame(gameId: Long): Flow<List<Player>>

    suspend fun createPlayer(player: Player, gameId: Long): Long

    suspend fun deletePlayer(playerId: Long)
}