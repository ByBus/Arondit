package host.capitalquiz.arondit.game.domain

data class Word(
    val word: String,
    val letterBonuses: List<Int> = emptyList(),
    val multiplier: Int = 1,
    val id: Long = 0,
) {
    fun score(rangDictionary: Map<Char, Int>): Int {
        var count = 0
        for ((i, char) in word.withIndex()) {
            val bonus = letterBonuses.getOrNull(i) ?: 1
            val current = rangDictionary[char] ?: 0
            count += bonus * current
        }
        return count * multiplier
    }

    fun <R> map(mapper: WordMapper<R>): R {
        return mapper.map(word, letterBonuses, multiplier, id)
    }
}