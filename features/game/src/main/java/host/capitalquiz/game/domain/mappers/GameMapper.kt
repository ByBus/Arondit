package host.capitalquiz.game.domain.mappers

import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.GameRuleSimple

interface GameMapper<R> {
    operator fun invoke(rule: GameRuleSimple, fields: List<Field>, winner: Field?): R
}