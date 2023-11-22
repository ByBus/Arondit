package host.capitalquiz.editgamerule.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface EditGameRuleInteractor {

    fun getGameRule(id: Long): Flow<GameRule>
    suspend fun createNewRule(name: String): Long
    suspend fun createCopyOfRule(namePrefix: String, rule: GameRule): Long
    suspend fun addLetterToRule(
        letter: Char,
        points: Int,
        ruleId: Long,
        replace: Boolean,
        namePrefix: String,
    ): LetterResult

    suspend fun renameRule(name: String, rule: GameRule)
    suspend fun editLetterOfRule(old: Char, new: Char, points: Int, ruleId: Long, namePrefix: String): LetterResult
    suspend fun deleteLetterFromRule(letter: Char, ruleId: Long, namePrefix: String): LetterResult

    class Base @Inject constructor(
        private val gameRuleRepository: EditGameRuleRepository,
        private val nextCharProvider: CharProvider,
    ) : EditGameRuleInteractor {
        override fun getGameRule(id: Long): Flow<GameRule> {
            return gameRuleRepository.findRuleById(id)
        }

        override suspend fun createNewRule(name: String): Long {
            return gameRuleRepository.createNewRule(name)
        }

        override suspend fun createCopyOfRule(namePrefix: String, rule: GameRule): Long {
            val id = gameRuleRepository.createNewRule("$namePrefix ${rule.name}")
            val emptyRule = gameRuleRepository.findRule(id)
            val newRule = emptyRule.copy(id = id, points = rule.points)
            gameRuleRepository.updateRule(newRule)
            return id
        }

        override suspend fun addLetterToRule(
            letter: Char,
            points: Int,
            ruleId: Long,
            replace: Boolean,
            namePrefix: String,
        ): LetterResult {
            var rule = gameRuleRepository.findRule(ruleId)

            if (rule.readOnly) {
                val newId = createCopyOfRule(namePrefix, rule)
                rule = gameRuleRepository.findRule(newId)
            }

            if (rule.hasLetter(letter) && replace.not())
                return LetterResult.AlreadyExist(letter, rule.points(letter), rule.id, points)

            val lettersToPoints = rule.points.toMutableMap()
            lettersToPoints[letter.uppercaseChar()] = points
            val newRule = rule.copy(points = lettersToPoints)
            gameRuleRepository.updateRule(newRule)

            val nextLetter = nextCharProvider.provide(newRule)
            return LetterResult.Success(nextLetter, rule.id)
        }

        override suspend fun renameRule(name: String, rule: GameRule) {
            val renamedRule = rule.copy(name = name)
            gameRuleRepository.updateRule(renamedRule)
        }

        override suspend fun editLetterOfRule(
            old: Char,
            new: Char,
            points: Int,
            ruleId: Long,
            namePrefix: String
        ): LetterResult {
            var rule = gameRuleRepository.findRule(ruleId)

            if (rule.readOnly) {
                val newId = createCopyOfRule(namePrefix, rule)
                rule = gameRuleRepository.findRule(newId)
            }

            if (rule.hasLetter(new) && old != new)
                return LetterResult.AlreadyExist(new, rule.points(old), rule.id, points)

            val updatedLetterToPoints = mutableMapOf<Char, Int>()
            rule.points.forEach { (letter, pts) ->
                if (letter.equals(old, true))
                    updatedLetterToPoints[new.uppercaseChar()] = points
                else
                    updatedLetterToPoints[letter] = pts
            }
            val newRule = rule.copy(points = updatedLetterToPoints)
            gameRuleRepository.updateRule(newRule)
            return LetterResult.Success(new, rule.id)
        }

        override suspend fun deleteLetterFromRule(letter: Char, ruleId: Long, namePrefix: String): LetterResult {
            var rule = gameRuleRepository.findRule(ruleId)

            if (rule.readOnly) {
                val newId = createCopyOfRule(namePrefix, rule)
                rule = gameRuleRepository.findRule(newId)
            }

            val newLettersPoints = rule.points.toMutableMap().apply {
                remove(letter.uppercaseChar())
            }
            val newRule = rule.copy(points = newLettersPoints)
            gameRuleRepository.updateRule(newRule)
            return LetterResult.Success(letter, rule.id)
        }
    }
}