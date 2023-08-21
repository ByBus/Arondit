package host.capitalquiz.arondit.game.data

import host.capitalquiz.arondit.core.db.WordData
import host.capitalquiz.arondit.core.db.WordDataMapper
import javax.inject.Inject

interface WordDataToWordDataMapper : WordDataMapper<WordData> {
    fun map(wordData: WordData, newWord: String): WordData

    class BonusUpdater @Inject constructor() : WordDataToWordDataMapper {
        private var newWord = ""
        override fun map(wordData: WordData, newWord: String): WordData {
            this.newWord = newWord
            return wordData.map(this)
        }

        override fun invoke(
            word: String,
            letterBonuses: List<Int>,
            multiplier: Int,
            id: Long,
            playerId: Long,
            extraPoints: Int
        ): WordData {
            val bonuses = shiftedBonusesForNewWord(newWord, word, letterBonuses)
            return WordData(newWord, bonuses, multiplier, playerId, extraPoints).apply {
                this.id = id
            }
        }

        private fun shiftedBonusesForNewWord(
            newWord: String,
            oldWord: String,
            oldBonuses: List<Int>,
        ): MutableList<Int> {
            var cursorOld = 0
            var cursorNew = 0
            val result = mutableListOf<Int>()

            val nextSameLetterHasScore = {
                val nextOldCursor = cursorOld + 1
                val nextNewCursor = cursorNew + 1
                nextNewCursor < newWord.length && nextOldCursor < oldWord.length &&
                        oldWord[nextOldCursor].equals(newWord[cursorNew], true) &&
                        !oldWord[nextOldCursor].equals(newWord[nextNewCursor], true) &&
                        oldBonuses[cursorOld] == 1 && oldBonuses[nextOldCursor] > 1
            }

            while (result.size < newWord.length) {
                when {
                    cursorOld >= oldWord.length -> {
                        result.add(1)
                        cursorNew++
                    }

                    oldWord[cursorOld].equals(newWord[cursorNew], true) -> {
                        if (!nextSameLetterHasScore()) {
                            result.add(oldBonuses[cursorOld])
                            cursorNew++
                        }
                        cursorOld++
                    }

                    newWord.length > oldWord.length -> {
                        result.add(1)
                        cursorNew++
                    }

                    newWord.length < oldWord.length -> cursorOld++

                    else -> {
                        result.add(1)
                        cursorNew++
                        cursorOld++
                    }
                }
            }
            return result
        }
    }
}