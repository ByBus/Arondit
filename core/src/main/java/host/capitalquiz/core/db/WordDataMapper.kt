package host.capitalquiz.core.db

interface WordDataMapper<R> {
    operator fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        playerId: Long,
        extraPoints: Int,
    ): R

}