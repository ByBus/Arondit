package host.capitalquiz.gameslist.domain

import kotlinx.coroutines.flow.Flow

interface GamesRepository {

    fun allGames(): Flow<List<Game>>

    suspend fun createGame(): Long

    suspend fun deleteGame(id: Long)

    suspend fun findGameFieldsIds(gameId: Long): List<Long>
}