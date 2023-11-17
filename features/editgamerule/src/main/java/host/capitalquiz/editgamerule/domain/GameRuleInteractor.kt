package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GameRuleInteractor {
    fun getAllRules(): Flow<List<GameRule>>
    suspend fun updateRuleForGame(gameId: Long, ruleId: Long)

    class Base @Inject constructor(
        private val gameRuleRepository: GameRuleRepository,
    ): GameRuleInteractor {
        override fun getAllRules(): Flow<List<GameRule>> {
            return gameRuleRepository.findAllRules()
        }

        override suspend fun updateRuleForGame(gameId: Long, ruleId: Long) {
            gameRuleRepository.setRuleForGame(gameId, ruleId)
        }
    }
}