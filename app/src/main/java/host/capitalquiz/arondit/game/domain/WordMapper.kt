package host.capitalquiz.arondit.game.domain

interface WordMapper<R> {

    fun map(word: Word): R

    operator fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        extraPoints: Int
    ): R

}