package host.capitalquiz.game.ui

import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.WordMapper
import host.capitalquiz.game.domain.WordMapperWithParameter
import javax.inject.Inject

class WordToWordUiMapper @Inject constructor() : WordMapperWithParameter<GameRuleSimple, WordUi> {
    private var score = 0

    override fun map(word: Word, param: GameRuleSimple): WordUi {
        score = word.score(param.dictionary)
        return word.map(this)
    }

    override fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        extraPoints: Int,
    ): WordUi {
        return WordUi(word, letterBonuses.toList(), multiplier, id, score, extraPoints)
    }
}
