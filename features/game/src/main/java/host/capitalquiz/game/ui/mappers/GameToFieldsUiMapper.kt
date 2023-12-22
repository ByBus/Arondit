package host.capitalquiz.game.ui.mappers

import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.mappers.FieldMapperWithParameter
import host.capitalquiz.game.domain.mappers.GameMapper
import host.capitalquiz.game.ui.FieldUi
import javax.inject.Inject

class GameToFieldsUiMapper @Inject constructor(
    private val fieldMapper: FieldMapperWithParameter<GameRuleSimple, FieldUi>,
) : GameMapper<List<@JvmSuppressWildcards FieldUi>> {

    override fun invoke(rule: GameRuleSimple, fields: List<Field>, winner: Field?): List<FieldUi> {
        return fields.map {
            fieldMapper.map(it, winner, rule)
        }
    }
}