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
    suspend fun addLetterToRule(letter: Char, points: Int, ruleId: Long, replace: Boolean, namePrefix: String): LetterAddResult
    suspend fun renameRule(name: String, rule: GameRule)

    class Base @Inject constructor(
        private val gameRuleRepository: EditGameRuleRepository,
        private val nextCharProvider: CharProvider
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

        override suspend fun addLetterToRule(
            letter: Char,
            points: Int,
            ruleId: Long,
            replace: Boolean,
            namePrefix: String
        ): LetterAddResult {
            return withContext(Dispatchers.IO) {
                var rule = gameRuleRepository.findRuleById(ruleId).first()

                if (rule.readOnly) {
                    val newId = createCopyOfRule(namePrefix, rule)
                    rule = gameRuleRepository.findRuleById(newId).first()
                }

                if (rule.hasLetter(letter) && replace.not()) {
                    return@withContext LetterAddResult.AlreadyExist(letter, rule.points(letter), rule.id)
                }

                val lettersToPoints = rule.points.toMutableMap()
                lettersToPoints[letter.uppercaseChar()] = points
                val newRule = rule.copy(points = lettersToPoints)
                gameRuleRepository.updateRule(newRule)

                val nextLetter = nextCharProvider.provide(newRule)
                LetterAddResult.Success(nextLetter, rule.id)
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