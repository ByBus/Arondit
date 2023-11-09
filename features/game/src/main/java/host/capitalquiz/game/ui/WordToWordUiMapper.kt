package host.capitalquiz.game.ui

import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.WordMapper
import javax.inject.Inject

class WordToWordUiMapper @Inject constructor(
    private val dictionary: Map<Char, Int>,
) : WordMapper<WordUi> {
    private var score = 0

    override fun map(word: Word): WordUi {
        score = word.score(dictionary)
        return word.map(this)
    }

    override fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        extraPoints: Int
    ): WordUi {
        return WordUi(word, letterBonuses.toList(), multiplier, id, score, extraPoints)
    }
}