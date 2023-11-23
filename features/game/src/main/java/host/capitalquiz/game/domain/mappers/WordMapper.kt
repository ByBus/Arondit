package host.capitalquiz.game.domain.mappers

interface WordMapper<R> {

    operator fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        extraPoints: Int
    ): R

}
