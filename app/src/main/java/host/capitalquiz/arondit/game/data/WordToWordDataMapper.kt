package host.capitalquiz.arondit.game.data

import host.capitalquiz.arondit.core.db.WordData
import host.capitalquiz.arondit.game.domain.Word
import host.capitalquiz.arondit.game.domain.WordMapperWithParameter
import javax.inject.Inject

class WordToWordDataMapper @Inject constructor() : WordMapperWithParameter<Long, Word, WordData> {
    private var playerId: Long = 0
    override fun map(word: Word, playerId: Long): WordData {
        this.playerId = playerId
        return word.map(this)
    }

    override fun map(word: Word): WordData {
        return word.map(this)
    }

    override fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        extraPoints: Int,
    ): WordData {
        val wordData =
            WordData(word.uppercase(), letterBonuses, multiplier, playerId, extraPoints).apply {
                this.id = id
            }
        return wordData
    }
}