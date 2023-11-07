package host.capitalquiz.game.domain

import javax.inject.Inject


interface WordToWordMapper: WordMapper<Word> {
    fun map(word: Word, newWord: String): Word

    class BonusUpdater @Inject constructor() : WordToWordMapper {
        private var newWord = ""
        override fun map(word: Word, newWord: String): Word {
            this.newWord = newWord
            return word.map(this)
        }

        override fun map(word: Word): Word {
            return word.map(this)
        }

        override fun invoke(
            word: String,
            letterBonuses: List<Int>,
            multiplier: Int,
            id: Long,
            extraPoints: Int
        ): Word {
            val bonuses = shiftedBonusesForNewWord(newWord, word, letterBonuses)
            return Word(newWord, bonuses, multiplier, id, extraPoints > 0)
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