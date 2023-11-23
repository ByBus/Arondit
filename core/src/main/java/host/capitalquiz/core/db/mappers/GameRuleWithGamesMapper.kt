package host.capitalquiz.core.db.mappers

import host.capitalquiz.core.db.GameData

interface GameRuleWithGamesMapper<R> {
    operator fun invoke(
        id: Long,
        name: String,
        points: Map<Char, Int>,
        readOnly: Boolean,
        games: List<GameData>,
    ): R
}
