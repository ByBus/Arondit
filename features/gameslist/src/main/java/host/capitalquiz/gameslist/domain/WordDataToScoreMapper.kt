package host.capitalquiz.gameslist.domain

import host.capitalquiz.core.db.WordDataMapper
import javax.inject.Inject


class WordDataToScoreMapper @Inject constructor(private val dictionary: Map<Char, Int>) :
    WordDataMapper<Int> {
    override fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        playerId: Long,
        extraPoints: Int
    ): Int {
        return word.map { letter -> (dictionary[letter] ?: 0) }
            .foldIndexed(0) { i, acc, current ->
                val bonus = letterBonuses.getOrNull(i) ?: 1
                acc + current * bonus
            } * multiplier + extraPoints
    }
}