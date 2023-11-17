package host.capitalquiz.game.data

import host.capitalquiz.core.db.GameRuleDataMapper
import host.capitalquiz.game.domain.GameRuleSimple
import javax.inject.Inject

class GameRuleDataToSimpleMapper @Inject constructor(): GameRuleDataMapper<GameRuleSimple> {
    override fun invoke(
        name: String,
        points: Map<Char, Int>,
        readOnly: Boolean,
        id: Long,
    ): GameRuleSimple {
        return GameRuleSimple(id, points)
    }
}