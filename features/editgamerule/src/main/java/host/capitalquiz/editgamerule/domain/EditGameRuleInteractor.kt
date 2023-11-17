package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface EditGameRuleInteractor {

    fun getGameRule(id: Long): Flow<GameRule>
    suspend fun createNewRule(name: String): Long

    class Base @Inject constructor(
        private val gameRuleRepository: EditGameRuleRepository
    ) : EditGameRuleInteractor {
        override fun getGameRule(id: Long): Flow<GameRule> {
            return gameRuleRepository.findRuleById(id)
        }

        override suspend fun createNewRule(name: String): Long {
            return gameRuleRepository.createNewRule(name)
        }
    }
}