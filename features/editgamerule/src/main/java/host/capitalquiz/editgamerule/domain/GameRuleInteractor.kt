package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GameRuleInteractor {
    fun getAllRules(): Flow<List<GameRule>>

    class Base @Inject constructor(private val gameRuleRepository: GameRuleRepository): GameRuleInteractor {
        override fun getAllRules(): Flow<List<GameRule>> {
            return gameRuleRepository.findAllRules()
        }
    }
}