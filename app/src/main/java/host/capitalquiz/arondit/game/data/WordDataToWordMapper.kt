package host.capitalquiz.arondit.game.data

import host.capitalquiz.arondit.core.db.WordDataMapper
import host.capitalquiz.arondit.game.domain.Word
import javax.inject.Inject

class WordDataToWordMapper @Inject constructor() : WordDataMapper<Word> {
    override fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        playerId: Long,
        extraPoints: Int
    ): Word {
        return Word(word, letterBonuses, multiplier, id, extraPoints > 0)
    }
}