package host.capitalquiz.editgamerule.ui.editscreen.mappers

import host.capitalquiz.editgamerule.domain.GameRuleMapper
import host.capitalquiz.editgamerule.ui.editscreen.EditableGameRuleUi
import host.capitalquiz.editgamerule.ui.editscreen.RuleLetter
import javax.inject.Inject

class EditableRuleToUiMapper @Inject constructor() : GameRuleMapper<EditableGameRuleUi> {
    override fun invoke(
        id: Long,
        name: String,
        points: Map<Char, Int>,
        readOnly: Boolean,
        gamesIds: List<Long>,
    ): EditableGameRuleUi {
        val letters = points.map { (char, points) -> RuleLetter(char, points) }
        return EditableGameRuleUi(id, name, readOnly, letters)
    }
}