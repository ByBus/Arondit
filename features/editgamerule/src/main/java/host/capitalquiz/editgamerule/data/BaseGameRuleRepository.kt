package host.capitalquiz.editgamerule.data

import host.capitalquiz.core.db.GameRuleWithGamesMapper
import host.capitalquiz.editgamerule.domain.GameRule
import host.capitalquiz.editgamerule.domain.GameRuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BaseGameRuleRepository @Inject constructor(
    private val gameRuleDataSource: GameRuleDataSource,
    private val gameDataSource: GameDataSource,
    private val mapper: GameRuleWithGamesMapper<GameRule>,
) : GameRuleRepository {
    override fun findAllRules(): Flow<List<GameRule>> {
        return gameRuleDataSource.getAllRules().map { rules ->
            rules.map { it.map(mapper) }
        }
    }

    override suspend fun deleteRule(id: Long) {
        gameRuleDataSource.removeRule(id)
    }

    override suspend fun setRuleForGame(gameId: Long, ruleId: Long,) {
        gameDataSource.setGameRuleToGame(gameId, ruleId)
    }

    override suspend fun saveRule(rule: GameRule) {
        TODO("Not yet implemented")
    }

    override suspend fun findGameRuleIdOfGame(gameId: Long): Long {
        return gameDataSource.gameById(gameId).ruleId
    }

}