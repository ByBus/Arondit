package host.capitalquiz.game.data.mappers

import host.capitalquiz.core.db.WordData
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.mappers.WordMapperWithParameter
import javax.inject.Inject

class WordToWordDataMapper @Inject constructor() : WordMapperWithParameter<Long, WordData> {
    private var playerId: Long = 0
    override fun map(word: Word, playerId: Long): WordData {
        this.playerId = playerId
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