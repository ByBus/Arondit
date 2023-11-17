package host.capitalquiz.editgamerule.ui.editscreen

import host.capitalquiz.editgamerule.domain.GameRule
import javax.inject.Inject

interface EditableGameRuleUiMapper<R> {
    operator fun invoke(id: Long, name: String, readOnly: Boolean, letters: List<RuleLetter>): R

    class ToGameRule @Inject constructor() : EditableGameRuleUiMapper<GameRule> {
        override fun invoke(
            id: Long,
            name: String,
            readOnly: Boolean,
            letters: List<RuleLetter>,
        ): GameRule {
            return GameRule(id, name,
                letters.associate { it.letter to it.points }, readOnly, emptyList()
            )
        }
    }
}