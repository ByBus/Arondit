package host.capitalquiz.editgamerule.data

import host.capitalquiz.core.db.GameDao
import host.capitalquiz.core.db.GameData
import javax.inject.Inject

interface GameDataSource {

    suspend fun setGameRuleToGame(gameId: Long, ruleId: Long)
    suspend fun gameById(gameId: Long): GameData
    suspend fun gamesWithRule(ruleId: Long): List<GameData>

    class Base @Inject constructor(
        private val gameDao: GameDao
    ): GameDataSource {
        override suspend fun setGameRuleToGame(gameId: Long, ruleId: Long) =
            gameDao.updateGameRule(gameId, ruleId)

        override suspend fun gameById(gameId: Long): GameData = gameDao.findGameById(gameId)

        override suspend fun gamesWithRule(ruleId: Long): List<GameData> =
            gameDao.findAllGamesWithRule(ruleId)
    }
}