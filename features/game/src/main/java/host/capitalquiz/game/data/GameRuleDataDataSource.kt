package host.capitalquiz.game.data

import host.capitalquiz.core.db.GameRuleDao
import host.capitalquiz.core.db.GameRuleDataMapper
import host.capitalquiz.game.domain.GameRuleSimple
import javax.inject.Inject

interface GameRuleDataDataSource {

    suspend fun findRule(gameId: Long): GameRuleSimple

    class Base @Inject constructor(
        private val gameRuleDao: GameRuleDao,
        private val ruleMapper: GameRuleDataMapper<GameRuleSimple>
    ): GameRuleDataDataSource {

        override suspend fun findRule(gameId: Long): GameRuleSimple {
            return gameRuleDao.findGameRuleByGameId(gameId).map(ruleMapper)
        }

    }
}