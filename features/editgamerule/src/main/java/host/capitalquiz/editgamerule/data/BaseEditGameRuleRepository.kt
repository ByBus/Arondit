package host.capitalquiz.editgamerule.data

import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.mappers.GameRuleDataMapper
import host.capitalquiz.editgamerule.domain.EditGameRuleRepository
import host.capitalquiz.editgamerule.domain.GameRule
import host.capitalquiz.editgamerule.domain.GameRuleMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BaseEditGameRuleRepository @Inject constructor(
    private val gameRuleDataSource: GameRuleDataSource,
    private val mapper: GameRuleDataMapper<GameRule>,
    private val dataMapper: GameRuleMapper<GameRuleData>
): EditGameRuleRepository {
    override fun findRuleById(id: Long): Flow<GameRule> {
        return gameRuleDataSource.getRuleById(id).map {
            it.map(mapper)
        }
    }

    override suspend fun findRule(id: Long): GameRule {
        return withContext(Dispatchers.IO) {
            gameRuleDataSource.getRuleById(id).first().map(mapper)
        }
    }

    override suspend fun createNewRule(name: String): Long =
        gameRuleDataSource.createRule(GameRuleData(name, emptyMap()))

    override suspend fun updateRule(rule: GameRule) {
        gameRuleDataSource.updateRule(rule.map(dataMapper))
    }
}