package host.capitalquiz.editgamerule.ui.ruleslist.mappers

import host.capitalquiz.editgamerule.domain.GameRule
import host.capitalquiz.editgamerule.domain.GameRuleMapperWithParameter
import host.capitalquiz.editgamerule.ui.ruleslist.GameRuleUi
import javax.inject.Inject

class GameRuleToGameUiMapper @Inject constructor(): GameRuleMapperWithParameter<Long, GameRuleUi> {
    private var ruleUsedByGame = false

    override fun map(game: GameRule, param: Long): GameRuleUi {
        ruleUsedByGame = game.usedByGame(param)
        return game.map(this)
    }

    override fun invoke(
        id: Long,
        name: String,
        points: Map<Char, Int>,
        readOnly: Boolean,
        gamesIds: List<Long>,
    ): GameRuleUi {
        return  GameRuleUi(id, name, points, readOnly, gamesIds, selected = ruleUsedByGame)
    }
}