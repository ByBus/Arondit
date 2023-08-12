package host.capitalquiz.arondit.game.domain

interface WordMapper<R> {

    fun map(word: Word): R
    fun map(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
    ): R
}