package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.flow.Flow

interface GameRuleRepository {
    fun findAllRules(): Flow<List<GameRule>>
    suspend fun deleteRule(id: Long)
    suspend fun setRuleForGame(gameId: Long, ruleId: Long)
    suspend fun findAllGamesIdsWithRule(ruleId: Long): List<Long>
}