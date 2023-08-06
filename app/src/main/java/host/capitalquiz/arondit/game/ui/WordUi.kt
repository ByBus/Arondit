package host.capitalquiz.arondit.game.ui

data class WordUi(
    val word: String = "",
    val letterBonuses: List<Int> = emptyList(),
    val multiplier: Int = 1,
    val id: Long = 0,
    val score: Int = 0,
) {
    fun <R> map(mapper: WordUiMapper<R>): R {
        return mapper(word, letterBonuses, multiplier, id, score)
    }
}
