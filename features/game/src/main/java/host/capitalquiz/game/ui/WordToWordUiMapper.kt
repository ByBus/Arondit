package host.capitalquiz.game.ui

import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.WordMapperWithParameter
import javax.inject.Inject

private const val NOT_ALLOWED_CHAR_BONUS = -1

class WordToWordUiMapper @Inject constructor() : WordMapperWithParameter<GameRuleSimple, WordUi> {
    private var score = 0
    private var dictionary = emptyMap<Char, Int>()

    override fun map(word: Word, param: GameRuleSimple): WordUi {
        dictionary = param.dictionary
        score = word.score(dictionary)
        return word.map(this)
    }

    override fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        extraPoints: Int,
    ): WordUi {
        val bonuses = mutableListOf<Int>()
        val letterScores = word.mapIndexed { i, char ->
            val score = dictionary.getOrDefault( char.uppercaseChar(), 0)
            bonuses.add(if (score != 0) letterBonuses[i] else NOT_ALLOWED_CHAR_BONUS)
            score
        }
        return WordUi(word, bonuses, multiplier, id, score, extraPoints, letterScores)
    }
}
