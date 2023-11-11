package host.capitalquiz.core.db

interface GameRuleDataMapper<R> {
    operator fun invoke(name: String, points: Map<Char, Int>, readOnly: Boolean, id: Long): R
}