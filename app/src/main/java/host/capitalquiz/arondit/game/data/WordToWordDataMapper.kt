package host.capitalquiz.arondit.game.data

import host.capitalquiz.arondit.core.db.WordData
import host.capitalquiz.arondit.game.domain.Word
import host.capitalquiz.arondit.game.domain.WordMapperWithId
import javax.inject.Inject

class WordToWordDataMapper @Inject constructor() : WordMapperWithId<Long, WordData> {
    override var additionalId: Long = 0
    override fun invoke(word: Word): WordData {
        return word.map(this)
    }

    override fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
    ): WordData {
        val wordData = WordData(word.uppercase(), letterBonuses, multiplier, additionalId).apply {
            this.id = id
        }
        return wordData
    }
}