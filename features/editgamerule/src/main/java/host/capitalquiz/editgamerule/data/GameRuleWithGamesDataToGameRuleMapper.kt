package host.capitalquiz.editgamerule.data

import host.capitalquiz.core.db.GameData
import host.capitalquiz.core.db.GameRuleWithGamesMapper
import host.capitalquiz.editgamerule.domain.GameRule
import javax.inject.Inject

class GameRuleWithGamesDataToGameRuleMapper @Inject constructor() :
    GameRuleWithGamesMapper<GameRule> {
    override fun invoke(
        id: Long,
        name: String,
        points: Map<Char, Int>,
        readOnly: Boolean,
        games: List<GameData>,
    ): GameRule {
        val ids = games.map { it.id }
        return GameRule(id, name, points, readOnly, ids)
    }
}