package host.capitalquiz.gameslist.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GamesListInteractor {
    fun allGames(): Flow<List<Game>>

    suspend fun deleteGame(gameId: Long)

    suspend fun createGame(): Long

    val showOnBoardingScreen: Flow<Boolean>

    class Base @Inject constructor(
        private val repository: GamesRepository,
        settings: SettingsReadRepository
    ): GamesListInteractor {

        override val showOnBoardingScreen: Flow<Boolean> = settings.showOnBoarding

        override fun allGames(): Flow<List<Game>> = repository.allGames()

        override suspend fun deleteGame(gameId: Long) = repository.deleteGame(gameId)

        override suspend fun createGame(): Long = repository.createGame()
    }
}