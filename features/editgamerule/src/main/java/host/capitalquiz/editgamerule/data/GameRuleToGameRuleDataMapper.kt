package host.capitalquiz.editgamerule.data

import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.editgamerule.domain.GameRuleMapper
import javax.inject.Inject

class GameRuleToGameRuleDataMapper @Inject constructor(): GameRuleMapper<GameRuleData> {
    override fun invoke(
        id: Long,
        name: String,
        points: Map<Char, Int>,
        readOnly: Boolean,
        gamesIds: List<Long>,
    ): GameRuleData {
        return GameRuleData(name, points, readOnly).apply {
            this.id = id
        }
    }
}