package host.capitalquiz.arondit.game.ui

import host.capitalquiz.arondit.game.domain.Word
import javax.inject.Inject

interface WordUiMapper<R> {
    operator fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        score: Int,
        extraPoints: Int
    ): R


    class Base @Inject constructor(): WordUiMapper<Word> {
        override fun invoke(
            word: String,
            letterBonuses: List<Int>,
            multiplier: Int,
            id: Long,
            score: Int,
            extraPoints: Int
        ): Word {
            return Word(word, letterBonuses, multiplier, id, extraPoints > 0)
        }
    }
}