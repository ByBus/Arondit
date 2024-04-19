package host.capitalquiz.game.domain

import host.capitalquiz.game.domain.mappers.WordMapper

private const val EXTRA_POINTS = 15

data class Word(
    val word: String,
    val letterBonuses: List<Int> = emptyList(),
    val multiplier: Int = 1,
    val id: Long = 0,
    val hasExtraPoints: Boolean = false,
) {
    fun score(rangDictionary: Map<Char, Int>): Int {
        var count = 0
        for ((i, char) in word.withIndex()) {
            val bonus = letterBonuses.getOrNull(i) ?: 1
            val current = rangDictionary[char] ?: 0
            count += bonus * current
        }
        return count * multiplier + extraPoints()
    }

    fun <R> map(mapper: WordMapper<R>): R {
        return mapper(word, letterBonuses, multiplier, id, extraPoints())
    }

    fun extraPoints() = if (hasExtraPoints && word.length > 7) EXTRA_POINTS else 0

}