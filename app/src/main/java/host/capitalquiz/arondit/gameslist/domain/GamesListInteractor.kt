package host.capitalquiz.arondit.gameslist.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject


interface GamesListInteractor {
    fun allGames(): LiveData<List<Game>>

    suspend fun deleteGame(gameId: Long)

    suspend fun createGame(): Long

    class Base @Inject constructor(private val repository: GamesRepository): GamesListInteractor {

        override fun allGames(): LiveData<List<Game>> = repository.allGames()

        override suspend fun deleteGame(gameId: Long) = repository.deleteGame(gameId)

        override suspend fun createGame(): Long = repository.createGame()
    }
}