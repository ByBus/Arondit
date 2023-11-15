package host.capitalquiz.editgamerule.domain

interface GameRuleMapper<R> {
    operator fun invoke(
        id: Long,
        name: String,
        points: Map<Char, Int>,
        readOnly: Boolean,
        gamesIds: List<Long>
    ): R
}

interface GameRuleMapperWithParameter<P, R> : GameRuleMapper<R> {
    fun map(game: GameRule, param: P): R
}