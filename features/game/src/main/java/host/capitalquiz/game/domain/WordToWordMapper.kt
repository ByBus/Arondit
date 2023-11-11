package host.capitalquiz.game.domain

import javax.inject.Inject


interface WordToWordMapper : WordMapper<Word> {
    fun map(word: Word, newWord: String, gameRule: GameRuleSimple): Word

    class BonusUpdater @Inject constructor() : WordToWordMapper {
        private var newWord = ""
        private lateinit var gameRule: GameRuleSimple

        override fun map(word: Word, newWord: String, gameRule: GameRuleSimple): Word {
            this.newWord = newWord.uppercase()
            this.gameRule = gameRule
            return word.map(this)
        }

        override fun invoke(
            word: String,
            letterBonuses: List<Int>,
            multiplier: Int,
            id: Long,
            extraPoints: Int,
        ): Word {
            val bonuses = shiftedBonusesForNewWord(newWord, word, letterBonuses)
            newWord.withIndex().forEach { (i, char) ->
                if (gameRule.dictionary.containsKey(char).not())
                    bonuses[i] = -1
            }
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