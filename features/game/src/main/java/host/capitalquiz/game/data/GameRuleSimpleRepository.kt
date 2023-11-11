package host.capitalquiz.game.data

import host.capitalquiz.game.domain.GameRuleRepository
import host.capitalquiz.game.domain.GameRuleSimple
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class GameRuleSimpleRepository @Inject constructor(
    private val gameRuleDataDataSource: GameRuleDataDataSource,
) : GameRuleRepository {
    private val mutex = Mutex()
    private var cached: GameRuleSimple? = null
    private var lastGameId = -1L

    override suspend fun gameRuleOfGame(gameId: Long): GameRuleSimple {
        return mutex.withLock {
            if (lastGameId != gameId) cached = null
            cached ?: gameRuleDataDataSource.findRule(gameId).apply {
                lastGameId = gameId
                cached = this
            }
        }
    }

    override fun readLastRule(): GameRuleSimple {
        return cached ?: throw IllegalStateException("No rule was cached")
    }
}