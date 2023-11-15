package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.flow.Flow

interface GameRuleRepository {
    fun findAllRules(): Flow<List<GameRule>>

    suspend fun deleteRule(id: Long)

    suspend fun setRuleForGame(ruleId: Long, gameId: Long)

    suspend fun saveRule(rule: GameRule)
}