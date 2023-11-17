package host.capitalquiz.editgamerule.data

import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.GameRuleDataMapper
import host.capitalquiz.editgamerule.domain.EditGameRuleRepository
import host.capitalquiz.editgamerule.domain.GameRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BaseEditGameRuleRepository @Inject constructor(
    private val gameRuleDataSource: GameRuleDataSource,
    private val mapper: GameRuleDataMapper<GameRule>
): EditGameRuleRepository {
    override fun findRuleById(id: Long): Flow<GameRule> {
        return gameRuleDataSource.getRuleById(id).map {
            it.map(mapper)
        }
    }

    override suspend fun createNewRule(name: String): Long {
        return gameRuleDataSource.createRule(GameRuleData(name, emptyMap()))
    }
}