package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val DEFAULT_GAME_RULE_ID = 1L

interface GameRuleInteractor {
    fun getAllRules(): Flow<List<GameRule>>
    suspend fun updateRuleForGame(gameId: Long, ruleId: Long)
    suspend fun deleteRule(ruleId: Long, currentGameId: Long)

    class Base @Inject constructor(
        private val gameRuleRepository: GameRuleRepository,
    ): GameRuleInteractor {
        override fun getAllRules(): Flow<List<GameRule>> {
            return gameRuleRepository.findAllRules()
        }

        override suspend fun updateRuleForGame(gameId: Long, ruleId: Long) {
            gameRuleRepository.setRuleForGame(gameId, ruleId)
        }

        override suspend fun deleteRule(ruleId: Long, currentGameId: Long) {
            val ruleIdOfCurrentGame = gameRuleRepository.findGameRuleIdOfGame(currentGameId)
            if (ruleId == ruleIdOfCurrentGame) {
                gameRuleRepository.setRuleForGame(currentGameId, DEFAULT_GAME_RULE_ID)
            }
            gameRuleRepository.deleteRule(ruleId)
        }
    }
}