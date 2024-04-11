package host.capitalquiz.core.ui.view.eruditwordview

import java.util.LinkedList
import java.util.Queue

class MayersWordUpdater<T> {
    private lateinit var grid: Array<IntArray>

    fun update(oldList: List<T>, newList: List<T>): List<T> {
        // y = newList, x = oldList
        val queue: Queue<Segment> = LinkedList()
        grid = generateField(oldList, newList)
        var last = Segment.initial()
        queue.offer(last)

        while (queue.isNotEmpty()) {
            val segment = queue.poll()!!
            val (x, y) = segment

            if (x == oldList.size && y == newList.size) {
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
        return applyDiff(oldList, newList, diff)
    }

    private fun Queue<Segment>.offerIfGridHasRoom(
        nextX: Int,
        nextY: Int,
        producer: () -> Segment,
    ) {
        if (nextY < grid.size && nextX < grid[0].size && grid[nextY][nextX] != PROCESSED)
            offer(producer.invoke())
    }

    private fun applyDiff(oldList: List<T>, newList: List<T>, diff: List<Segment>): List<T> {
        val result = mutableListOf<T>()
        var cursorOld = 0
        var cursorNew = 0
        for (segment in diff) {
            when {
                segment.isRetain -> result.add(oldList[cursorOld++]).also { cursorNew++ }
                segment.isAdd -> result.add(newList[cursorNew++])
                else -> cursorOld++
            }
        }
        return result
    }

    private fun generateField(oldList: List<T>, newList: List<T>): Array<IntArray> {
        val grid = Array(newList.size + 1) { IntArray(oldList.size + 1) }
        for (y in newList.indices) {
            for (x in oldList.indices) {
                try {
                    val a = newList[y] as Char
                    val b = oldList[x] as Char
                    if (a.equals(b, true)) {
                        grid[y][x] = SHORTCUT
                    }
                } catch (e: ClassCastException) {
                    if (newList[y] == oldList[x]) {
                        grid[y][x] = SHORTCUT
                    }
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