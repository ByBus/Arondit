package host.capitalquiz.editgamerule.data

import host.capitalquiz.core.db.GameRuleDao
import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.GameRuleWithGamesData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GameRuleDataSource {
    fun getAllRules(): Flow<List<GameRuleWithGamesData>>
    fun getRuleById(id: Long): Flow<GameRuleData>
    suspend fun removeRule(id: Long)

    class Base @Inject constructor(
        private val ruleDao: GameRuleDao
    ) : GameRuleDataSource {
        override fun getAllRules(): Flow<List<GameRuleWithGamesData>> = ruleDao.findAllGameRules()

        override fun getRuleById(id: Long): Flow<GameRuleData> = ruleDao.findGameRuleById(id)

        override suspend fun removeRule(id: Long) = ruleDao.deleteGameRuleById(id)
    }
}