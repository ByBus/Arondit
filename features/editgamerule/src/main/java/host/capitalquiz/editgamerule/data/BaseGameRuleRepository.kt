package host.capitalquiz.editgamerule.data

import host.capitalquiz.core.db.GameRuleWithGamesMapper
import host.capitalquiz.editgamerule.domain.GameRule
import host.capitalquiz.editgamerule.domain.GameRuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BaseGameRuleRepository @Inject constructor(
    private val gameRuleDataSource: GameRuleDataSource,
    private val mapper: GameRuleWithGamesMapper<GameRule>
) : GameRuleRepository {
    override fun findAllRules(): Flow<List<GameRule>> {
        return gameRuleDataSource.getAllRules().map { rules ->
            rules.map { it.map(mapper) }
        }
    }

    override suspend fun deleteRule(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun setRuleForGame(ruleId: Long, gameId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun saveRule(rule: GameRule) {
        TODO("Not yet implemented")
    }

}