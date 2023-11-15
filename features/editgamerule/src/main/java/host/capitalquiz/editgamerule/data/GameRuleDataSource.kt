package host.capitalquiz.editgamerule.data

import host.capitalquiz.core.db.GameRuleDao
import host.capitalquiz.core.db.GameRuleWithGamesData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GameRuleDataSource {
    fun getAllRules(): Flow<List<GameRuleWithGamesData>>

    class Base @Inject constructor(
        private val ruleDao: GameRuleDao
    ) : GameRuleDataSource {
        override fun getAllRules(): Flow<List<GameRuleWithGamesData>> = ruleDao.findAllGameRules()
    }
}