package host.capitalquiz.game.domain

import host.capitalquiz.game.domain.mappers.GameMapper

data class Game(
    private val rule: GameRuleSimple,
    private val fields: List<Field>,
) {
    fun <R> map(mapper: GameMapper<R>): R {
        return mapper(rule, fields, winner())
    }

    private fun winner(): Field? {
        if (fields.isEmpty()) return null

        val winners = fields.groupBy {
            it.score
        }.maxBy { it.key }.value

        if (winners.size > 1 || winners.size == fields.size) return null

        return winners.first()
    }
}