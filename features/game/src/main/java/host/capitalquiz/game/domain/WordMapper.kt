package host.capitalquiz.game.domain

interface WordMapper<R> {

    operator fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        extraPoints: Int
    ): R

}
