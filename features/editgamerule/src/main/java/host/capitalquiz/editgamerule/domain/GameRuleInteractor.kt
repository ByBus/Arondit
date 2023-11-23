package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val DEFAULT_GAME_RULE_ID = 1L

interface GameRuleInteractor {
    fun getAllRules(): Flow<List<GameRule>>
    suspend fun updateRuleForGame(gameId: Long, ruleId: Long)
    suspend fun deleteRule(ruleId: Long, currentGameId: Long): Boolean

    class Base @Inject constructor(
        private val gameRuleRepository: GameRuleRepository,
    ): GameRuleInteractor {
        override fun getAllRules(): Flow<List<GameRule>> {
            return gameRuleRepository.findAllRules()
        }

        override suspend fun updateRuleForGame(gameId: Long, ruleId: Long) {
            gameRuleRepository.setRuleForGame(gameId, ruleId)
        }

        override suspend fun deleteRule(ruleId: Long, currentGameId: Long): Boolean {
            val gamesIdsUsingRule = gameRuleRepository.findAllGamesIdsWithRule(ruleId)
            val restGamesCount = gamesIdsUsingRule.filterNot { it == currentGameId }.count()

            if (restGamesCount > 0) return false

            if (currentGameId in gamesIdsUsingRule) {
                gameRuleRepository.setRuleForGame(currentGameId, DEFAULT_GAME_RULE_ID)
            }
            gameRuleRepository.deleteRule(ruleId)
            return true
        }
    }
}