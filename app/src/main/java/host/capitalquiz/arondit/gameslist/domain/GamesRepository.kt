package host.capitalquiz.arondit.gameslist.domain

import androidx.lifecycle.LiveData

interface GamesRepository {

    fun allGames(): LiveData<List<Game>>

    suspend fun createGame(): Long

    suspend fun deleteGame(id: Long)
}