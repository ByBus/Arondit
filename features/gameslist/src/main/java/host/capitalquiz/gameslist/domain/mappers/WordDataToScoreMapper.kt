package host.capitalquiz.gameslist.domain.mappers

import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.mappers.WordDataMapperWithParameter
import javax.inject.Inject


class WordDataToScoreMapper @Inject constructor() :
    WordDataMapperWithParameter<@JvmSuppressWildcards Map<Char, Int>, Int> {
    private var dictionary = mapOf<Char, Int>()

    override fun map(word: WordData, param: Map<Char, Int>): Int {
        dictionary = param
        return word.map(this)
    }

    override fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        playerId: Long,
        extraPoints: Int,
    ): Int {
        return word.map { letter -> (dictionary[letter] ?: 0) }
            .foldIndexed(0) { i, acc, current ->
                val bonus = letterBonuses.getOrNull(i) ?: 1
                acc + current * bonus
            } * multiplier + extraPoints
    }
}