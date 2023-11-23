package host.capitalquiz.editgamerule.data.mappers

import host.capitalquiz.core.db.mappers.GameRuleDataMapper
import host.capitalquiz.editgamerule.domain.GameRule
import javax.inject.Inject

class GameRuleDataToGameRuleMapper @Inject constructor(): GameRuleDataMapper<GameRule> {
    override fun invoke(
        name: String,
        points: Map<Char, Int>,
        readOnly: Boolean,
        id: Long,
    ): GameRule {
        return GameRule(id, name, points, readOnly, emptyList())
    }
}