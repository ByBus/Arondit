package host.capitalquiz.game.ui.mappers

import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.mappers.FieldMapperWithParameter
import host.capitalquiz.game.domain.mappers.WordMapperWithParameter
import host.capitalquiz.game.ui.FieldUi
import host.capitalquiz.game.ui.WordUi
import javax.inject.Inject

class FieldToFieldUiMapper @Inject constructor(
    private val wordMapper: WordMapperWithParameter<GameRuleSimple, WordUi>,
) : FieldMapperWithParameter<GameRuleSimple, FieldUi> {
    private var rule: GameRuleSimple = GameRuleSimple(1L, emptyMap())
    private var winner: Boolean = false

    override fun map(field: Field, winner: Field?, rule: GameRuleSimple): FieldUi {
        this.rule = rule
        this.winner = field == winner
        return field.map(this)
    }

    override fun invoke(
        id: Long,
        name: String,
        color: Int,
        score: Int,
        words: List<Word>,
        playerId: Long,
    ): FieldUi {
        return FieldUi(
            id,
            color,
            name,
            words.map { wordMapper.map(it, rule) },
            score,
            playerId,
            winner
        )
    }
}