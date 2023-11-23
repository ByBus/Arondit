package host.capitalquiz.game.data.mappers

import host.capitalquiz.core.db.mappers.GameRuleDataMapper
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