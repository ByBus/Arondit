package host.capitalquiz.game.domain

import androidx.lifecycle.LiveData

interface PlayerRepository {

    fun allPlayersOfGame(gameId: Long): LiveData<List<Player>>

    suspend fun createPlayer(player: Player, gameId: Long): Long

    suspend fun deletePlayer(playerId: Long)
}