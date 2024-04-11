package host.capitalquiz.game.domain.mappers

import host.capitalquiz.game.domain.Word
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject


interface WordToWordMapper : WordMapper<Word> {
    fun map(word: Word, newWord: String): Word

    class BonusUpdater @Inject constructor() : WordToWordMapper {
        private lateinit var grid: Array<IntArray>
        private var newWord = ""

        override fun map(word: Word, newWord: String): Word {
            this.newWord = newWord.uppercase()
            return word.map(this)
        }

        override fun invoke(
            word: String,
            letterBonuses: List<Int>,
            multiplier: Int,
            id: Long,
            extraPoints: Int,
        ): Word {
            val newBonuses = update(word, newWord, letterBonuses)
            return Word(newWord, newBonuses, multiplier, id, extraPoints > 0).also {
                grid = emptyArray()
            }
        }

        // Mayers diff algorithm
        private fun update(
            oldWord: String,
            newWord: String,
            oldBonuses: List<Int>,
        ): List<Int> {
            // y = newList, x = oldList
            val queue: Queue<Segment> = LinkedList()
            grid = generateField(oldWord, newWord)
            var last = Segment.initial()
            queue.offer(last)

            while (queue.isNotEmpty()) {
                val segment = queue.poll()!!
                val (x, y) = segment

                if (x == oldWord.length && y == newWord.length) {
                    last = segment
                    break
                }
                if (grid[y][x] == SHORTCUT) {
                    queue.offerIfGridHasRoom(x + 1, y + 1, segment::withRetainOld)
                } else {
                    queue.offerIfGridHasRoom(x + 1, y, segment::withRemoveOld)
                    queue.offerIfGridHasRoom(x, y + 1, segment::withAddNew)
                }
                grid[y][x] = PROCESSED
            }
            val diff = findDiff(last)
            return applyDiff(oldBonuses, diff)
        }

        private fun Queue<Segment>.offerIfGridHasRoom(
            nextX: Int,
            nextY: Int,
            producer: () -> Segment,
        ) {
            if (nextY < grid.size && nextX < grid[0].size && grid[nextY][nextX] != PROCESSED)
                offer(producer.invoke())
        }

        private fun applyDiff(
            oldList: List<Int>,
            diff: List<Segment>,
        ): List<Int> {
            val result = mutableListOf<Int>()
            var cursorOld = 0
            for (segment in diff) {
                when {
                    segment.isRetain -> result.add(oldList[cursorOld++])
                    segment.isAdd -> result.add(DEFAULT_BONUS_FOR_NEW_LETTERS)
                    else -> cursorOld++
                }
            }
            return result
        }

        private fun generateField(oldList: String, newList: String): Array<IntArray> {
            val grid = Array(newList.length + 1) { IntArray(oldList.length + 1) }
            for (y in newList.indices) {
                for (x in oldList.indices) {
                    if (newList[y] == oldList[x]) {
                        grid[y][x] = SHORTCUT
                    }
                }
            }
            return grid
        }

        private fun findDiff(segment: Segment): List<Segment> {
            var current = segment
            val diff = mutableListOf<Segment>()
            while (current.parent != null) {
                diff.add(0, current)
                current = current.parent!!
            }
            return diff
        }

        companion object {
            private const val PROCESSED = 1
            private const val SHORTCUT = -1
            private const val DEFAULT_BONUS_FOR_NEW_LETTERS = 1
        }

        private class Segment(
            private val x: Int,
            private val y: Int,
            operation: Int,
            val parent: Segment? = null,
        ) {
            val isAdd = operation == ADD_NEW
            val isRetain = operation == RETAIN_OLD

            fun withAddNew(): Segment = Segment(x, y + 1, ADD_NEW, this)

            fun withRetainOld(): Segment = Segment(x + 1, y + 1, RETAIN_OLD, this)

            fun withRemoveOld(): Segment = Segment(x + 1, y, REMOVE, this)

            operator fun component1() = x
            operator fun component2() = y

            companion object {
                private const val RETAIN_OLD = 0
                private const val ADD_NEW = 1
                private const val REMOVE = -1
                fun initial(): Segment = Segment(0, 0, RETAIN_OLD)
            }
        }
    }
}