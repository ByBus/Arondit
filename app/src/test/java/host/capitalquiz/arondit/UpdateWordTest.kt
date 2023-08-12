package host.capitalquiz.arondit

import org.junit.Assert
import org.junit.Test

class UpdateWordTest {

    @Test
    fun `test add one letter into word`() {
        val oldWord = "ПОРОХ"
        val oldBonuses = mutableListOf(1, 2, 1, 2, 1)

        val newWORD = "ПОТРОХ"
        val expected = mutableListOf(1, 2, 1, 1, 2, 1)

        val actual = updatedBonuses(newWORD, oldWord, oldBonuses)
        println("expected=$expected actual=$actual")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test add two letter into word`() {
        val oldWord = "ПОРОХ"
        val oldBonuses = mutableListOf(1, 2, 1, 2, 1)

        val newWORD = "ПОТРОХА"
        val expected = mutableListOf(1, 2, 1, 1, 2, 1, 1)

        val actual = updatedBonuses(newWORD, oldWord, oldBonuses)
        println("expected=$expected actual=$actual")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test remove one letter`() {
        val oldWord = "ПОРОХ"
        val oldBonuses = mutableListOf(1, 2, 1, 2, 1)

        val newWORD = "ПООХ"
        val expected = mutableListOf(1, 2, 2, 1)

        val actual = updatedBonuses(newWORD, oldWord, oldBonuses)
        println("expected=$expected actual=$actual")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test remove two letter`() {
        val oldWord = "ПОРОХИC"
        val oldBonuses = mutableListOf(1, 2, 1, 2, 1, 3, 2)

        val newWORD = "ПООХИ"
        val expected = mutableListOf(1, 2, 2, 1, 3)

        val actual = updatedBonuses(newWORD, oldWord, oldBonuses)
        println("expected=$expected actual=$actual")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test remove three letter`() {
        val oldWord = "ПОРОХИC"
        val oldBonuses = mutableListOf(1, 2, 1, 2, 1, 3, 2)

        val newWORD = "ПОХИ"
        val expected = mutableListOf(1, 2, 1, 3)

        val actual = updatedBonuses(newWORD, oldWord, oldBonuses)
        println("expected=$expected actual=$actual")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test remove last letter`() {
        val oldWord = "ПРОЛОГ"
        val oldBonuses = mutableListOf(1, 1, 2, 1, 1, 1)

        val newWORD = "ПРОЛО"
        val expected = mutableListOf(1, 1, 2, 1, 1)

        val actual = updatedBonuses(newWORD, oldWord, oldBonuses)
        println("expected=$expected actual=$actual")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test remove first letters`() {
        val oldWord = "ППРОЛО"
        val oldBonuses = mutableListOf(1, 3, 1, 1, 2, 1)

        val newWORD = "ПРОЛО"
        val expected = mutableListOf(3, 1, 1, 2, 1)

        val actual = updatedBonuses(newWORD, oldWord, oldBonuses)
        println("expected=$expected actual=$actual")
        Assert.assertEquals(
            expected, actual
        ) // It's unknown which letter, 1st or 2nd, was removed
    }

    private fun updatedBonuses(
        newWord: String,
        oldWord: String,
        oldBonuses: MutableList<Int>,
    ): MutableList<Int> {
        var cursorOld = 0
        var cursorNew = 0

        val actual = mutableListOf<Int>()
        while (actual.size < newWord.length) {
            when {
                cursorOld >= oldWord.length -> {
                    actual.add(1)
                    cursorNew++
                }

                oldWord[cursorOld].equals(newWord[cursorNew], true) -> {
                    val nextOldCursor = cursorOld + 1
                    val nextNewCursor = cursorNew + 1
                    val nextSameLetterHasScore = nextOldCursor < oldWord.length &&
                            oldWord[nextOldCursor].equals(newWord[cursorNew], true) &&
                            nextNewCursor < newWord.length &&
                            !oldWord[nextOldCursor].equals(newWord[nextNewCursor], true) &&
                            oldBonuses[cursorOld] == 1 && oldBonuses[nextOldCursor] > 1

                    if (!nextSameLetterHasScore) {
                        actual.add(oldBonuses[cursorOld])
                        cursorNew++
                    }
                    cursorOld++
                }

                newWord.length > oldWord.length -> {
                    actual.add(1)
                    cursorNew++
                }

                newWord.length < oldWord.length -> cursorOld++

                else -> {
                    actual.add(1)
                    cursorNew++
                    cursorOld++
                }
            }
        }
        return actual
    }


}