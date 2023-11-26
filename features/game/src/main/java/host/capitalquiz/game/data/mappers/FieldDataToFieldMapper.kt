package host.capitalquiz.game.data.mappers

import host.capitalquiz.core.db.FieldData
import host.capitalquiz.core.db.FieldWithPlayerAndWordsData
import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.mappers.FieldDataMapperWithParameter
import host.capitalquiz.core.db.mappers.WordDataMapper
import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Word
import javax.inject.Inject


class FieldDataToFieldMapper @Inject constructor(
    private val wordMapper: WordDataMapper<Word>,
) : FieldDataMapperWithParameter<GameRuleSimple, Field> {
    private var dictionary: Map<Char, Int> = emptyMap()

    override fun map(field: FieldWithPlayerAndWordsData, param: GameRuleSimple): Field {
        dictionary = param.dictionary
        return field.map(this)
    }

    override fun invoke(player: PlayerData, words: List<WordData>, field: FieldData): Field {
        val newWords = words.map { it.map(wordMapper) }
        return Field(
            field.id,
            player.name,
            field.color,
            newWords.sumOf { it.score(dictionary) },
            newWords,
            field.playerId
        )
    }
}