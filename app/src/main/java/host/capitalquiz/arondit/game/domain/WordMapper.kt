package host.capitalquiz.arondit.game.domain

interface WordMapper<R> {

    operator fun invoke(word: Word): R
    operator fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
    ): R
}