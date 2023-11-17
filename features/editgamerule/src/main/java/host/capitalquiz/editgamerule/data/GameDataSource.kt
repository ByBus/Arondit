package host.capitalquiz.editgamerule.data

import host.capitalquiz.core.db.GameDao
import javax.inject.Inject

interface GameDataSource {

    suspend fun setGameRuleToGame(gameId: Long, ruleId: Long)

    class Base @Inject constructor(
        private val gameDao: GameDao
    ): GameDataSource {
        override suspend fun setGameRuleToGame(gameId: Long, ruleId: Long) {
            gameDao.updateGameRule(gameId, ruleId)
        }
    }
}