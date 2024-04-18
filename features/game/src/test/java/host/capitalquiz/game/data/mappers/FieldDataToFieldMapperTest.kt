package host.capitalquiz.game.data.mappers

import host.capitalquiz.core.db.FieldData
import host.capitalquiz.core.db.FieldWithPlayerAndWordsData
import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.mappers.WordDataMapper
import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Word
import org.junit.Assert.assertEquals
import org.junit.Test

class FieldDataToFieldMapperTest {

    @Test
    fun `map FieldData to Field`() {
        val fieldMapper = FieldDataToFieldMapper(FakeWordDataMapper())

        val gameId = 1L
        val fieldID = 2L
        val playerId = 5L

        val fieldData = FieldWithPlayerAndWordsData(
            field = FieldData(0xFFF, gameId, playerId).also { it.id = fieldID },
            player = PlayerData("Player").also { it.id = playerId },
            words = listOf(WordData("word", fieldId = fieldID))
        )
        val simpleGameRule = GameRuleSimple(
            id = 4L,
            dictionary = mapOf('W' to 1, 'O' to 3, 'R' to 2, 'D' to 4)
        )


        val actual = fieldMapper.map(fieldData, simpleGameRule)
        val expected = Field(
            id = fieldID,
            name = "Player",
            color = 0xFFF,
            score = 10, // 1 + 3 + 2 + 4
            words = listOf(
                Word("FAKE WORD", emptyList(), 1, 0, false)
            ),
            playerId
        )
        assertEquals(expected, actual)
    }
}


class FakeWordDataMapper : WordDataMapper<Word> {
    override fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        playerId: Long,
        extraPoints: Int,
    ): Word = Word(
        word = "FAKE WORD",
        letterBonuses = emptyList(),
        multiplier = 1,
        id = 0,
        hasExtraPoints = false
    )
}