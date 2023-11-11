package host.capitalquiz.game.domain

import javax.inject.Inject

interface GameRuleInteractor {

    suspend fun findRuleOfGame(gameId: Long): GameRuleSimple

    fun getLastGameRule(): GameRuleSimple

    class Base @Inject constructor(
        private val repository: GameRuleRepository,
    ) : GameRuleInteractor {
        override suspend fun findRuleOfGame(gameId: Long): GameRuleSimple {
            return repository.gameRuleOfGame(gameId)
        }

        override fun getLastGameRule(): GameRuleSimple {
            return repository.readLastRule()
        }
    }
}