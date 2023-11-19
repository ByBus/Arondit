package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface EditGameRuleInteractor {

    fun getGameRule(id: Long): Flow<GameRule>
    suspend fun createNewRule(name: String): Long
    suspend fun createCopyOfRule(namePrefix: String, rule: GameRule): Long
    suspend fun addLetterToRule(letter: Char, points: Int, ruleId: Long)
    suspend fun renameRule(name: String, rule: GameRule)

    class Base @Inject constructor(
        private val gameRuleRepository: EditGameRuleRepository,
    ) : EditGameRuleInteractor {
        override fun getGameRule(id: Long): Flow<GameRule> {
            return gameRuleRepository.findRuleById(id)
        }

        override suspend fun createNewRule(name: String): Long {
            return gameRuleRepository.createNewRule(name)
        }

        override suspend fun createCopyOfRule(namePrefix: String, rule: GameRule): Long {
            return withContext(Dispatchers.IO) {
                val id = gameRuleRepository.createNewRule("$namePrefix ${rule.name}")
                val emptyRule = getGameRule(id).first()
                val newRule = emptyRule.copy(id = id, points = rule.points)
                gameRuleRepository.updateRule(newRule)
                id
            }
        }

        override suspend fun addLetterToRule(letter: Char, points: Int, ruleId: Long) {
            return withContext(Dispatchers.IO) {
                val rule = gameRuleRepository.findRuleById(ruleId).first()
                val lettersToPoints = rule.points.toMutableMap()
                lettersToPoints[letter.uppercaseChar()] = points
                val newRule = rule.copy(points = lettersToPoints)
                gameRuleRepository.updateRule(newRule)
            }
        }

        override suspend fun renameRule(name: String, rule: GameRule) {
            withContext(Dispatchers.IO) {
                val renamedRule = rule.copy(name = name)
                gameRuleRepository.updateRule(renamedRule)
            }
        }
    }
}