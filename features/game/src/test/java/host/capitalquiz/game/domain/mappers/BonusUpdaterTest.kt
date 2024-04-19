package host.capitalquiz.game.domain.mappers

import host.capitalquiz.game.domain.Word
import org.junit.Assert.assertEquals
import org.junit.Test

class BonusUpdaterTest {

    @Test
    fun `update bonuses on add new letters at the end`() {
        val bonusUpdater = WordToWordMapper.BonusUpdater()
        val oldWord = Word("РОТ", letterBonuses = listOf(1, 5, 3))
        val actual = bonusUpdater.map(oldWord, "РОТОР")

        val expected = Word("РОТОР", letterBonuses = listOf(1, 5, 3, 1, 1))

        assertEquals(expected, actual)
    }

    @Test
    fun `update bonuses on add new letters in the middle`() {
        val bonusUpdater = WordToWordMapper.BonusUpdater()
        val oldWord = Word("МОР", letterBonuses = listOf(1, 5, 3))
        val actual = bonusUpdater.map(oldWord, "МОТОР")

        val expected = Word("МОТОР", letterBonuses = listOf(1, 5, 1, 1, 3))

        assertEquals(expected, actual)
    }

    @Test
    fun `update bonuses on add new letters at the beginning`() {
        val bonusUpdater = WordToWordMapper.BonusUpdater()
        val oldWord = Word("РОМ", letterBonuses = listOf(2, 5, 3))
        val actual = bonusUpdater.map(oldWord, "ПАРОМ")

        val expected = Word("ПАРОМ", letterBonuses = listOf(1, 1, 2, 5, 3))

        assertEquals(expected, actual)
    }

    @Test
    fun `update bonuses on remove letters`() {
        val bonusUpdater = WordToWordMapper.BonusUpdater()
        val oldWord = Word("ХОРОМЫ", letterBonuses = listOf(8, 2, 3, 2, 5, 6))
        val actual = bonusUpdater.map(oldWord, "ХРОМ")

        val expected = Word("ХРОМ", letterBonuses = listOf(8, 3, 2, 5))

        assertEquals(expected, actual)
    }

    @Test
    fun `update bonuses of a word when new word is partially identical`() {
        val bonusUpdater = WordToWordMapper.BonusUpdater()
        val oldWord = Word("ХОРОМЫ", letterBonuses = listOf(8, 2, 3, 2, 5, 6))
        val actual = bonusUpdater.map(oldWord, "ХОБОТ")

        val expected = Word("ХОБОТ", letterBonuses = listOf(8, 2, 1, 2, 1))

        assertEquals(expected, actual)
    }
}