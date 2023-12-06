package host.capitalquiz.gameslist.domain

import host.capitalquiz.core.interfaces.DeleteFieldUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface GamesListInteractor {
    fun allGames(): Flow<List<Game>>

    suspend fun deleteGame(gameId: Long)

    suspend fun createGame(): Long

    val showOnBoardingScreen: Flow<Boolean>

    class Base @Inject constructor(
        private val repository: GamesRepository,
        private val deleteFieldUseCase: DeleteFieldUseCase,
        settings: SettingsReadRepository,
    ): GamesListInteractor {

        override val showOnBoardingScreen: Flow<Boolean> = settings.showOnBoarding

        override fun allGames(): Flow<List<Game>> = repository.allGames()

        override suspend fun deleteGame(gameId: Long) {
            val fields = repository.findGameFieldsIds(gameId)
            fields.forEach { fieldId ->
                deleteFieldUseCase(fieldId)
            }
            repository.deleteGame(gameId)
        }

        override suspend fun createGame(): Long = repository.createGame()
    }
}