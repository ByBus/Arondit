package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface EditGameRuleInteractor {

    fun getGameRule(id: Long): Flow<GameRule>

    class Base @Inject constructor(
        private val gameRuleRepository: EditGameRuleRepository
    ) : EditGameRuleInteractor {
        override fun getGameRule(id: Long): Flow<GameRule> = gameRuleRepository.findRuleById(id)
    }
}