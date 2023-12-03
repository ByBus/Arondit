package host.capitalquiz.statistics.ui

interface Sorter {
    fun sort(items: List<UserStatsUi>): List<UserStatsUi>

    fun invertedOrThis(sorter: Sorter): Sorter

    fun <R> map(mapper: SorterMapper<R>): R

    abstract class BaseSorter<T : Comparable<T>>(protected var ascendant: Boolean) : Sorter {
        protected abstract fun inverted(): Sorter
        protected abstract val selector: (UserStatsUi) -> T

        override fun invertedOrThis(sorter: Sorter): Sorter {
            return if (this::class == sorter::class)
                if (ascendant) Default else inverted()
            else
                sorter
        }

        override fun sort(items: List<UserStatsUi>): List<UserStatsUi> {
            return if (ascendant)
                items.sortedBy(selector)
            else
                items.sortedByDescending(selector)
        }

        override fun <R> map(mapper: SorterMapper<R>): R = mapper(if (ascendant) 1 else -1)
    }

    abstract class BaseStringSorter<T>(ascendant: Boolean) : BaseSorter<T>(ascendant)
            where T : CharSequence, T : Comparable<T> {
        override fun invertedOrThis(sorter: Sorter): Sorter {
            return if (this::class == sorter::class)
                if (ascendant) inverted() else Default
            else
                sorter
        }
    }

    object Default : BaseStringSorter<String>(true) {
        override val selector = UserStatsUi::playerName
        override fun inverted(): Sorter = this
        override fun sort(items: List<UserStatsUi>): List<UserStatsUi> = items.toList()
        override fun invertedOrThis(sorter: Sorter): Sorter = sorter
        override fun <R> map(mapper: SorterMapper<R>): R = mapper(0)
    }

    class TotalGames(order: Boolean = false) : BaseSorter<Int>(order) {
        override fun inverted(): Sorter = TotalGames(true)
        override val selector = UserStatsUi::totalGames
    }

    class Victories(order: Boolean = false) : BaseSorter<Int>(order) {
        override fun inverted(): Sorter = Victories(true)
        override val selector = UserStatsUi::victories
    }

    class VictoriesRate(order: Boolean = false) : BaseSorter<Double>(order) {
        override fun inverted(): Sorter = VictoriesRate(true)
        override val selector = UserStatsUi::victoriesRate
    }

    class WordsCount(order: Boolean = false) : BaseSorter<Int>(order) {
        override fun inverted(): Sorter = WordsCount(true)
        override val selector = UserStatsUi::words
    }

    class WordsPerGame(order: Boolean = false) : BaseSorter<Double>(order) {
        override fun inverted(): Sorter = WordsPerGame(true)
        override val selector = UserStatsUi::wordsPerGame
    }

    class MaxWordsInGame(order: Boolean = false) : BaseSorter<Int>(order) {
        override fun inverted(): Sorter = MaxWordsInGame(true)
        override val selector = UserStatsUi::maxWordsInGame
    }

    class ScorePerGame(order: Boolean = false) : BaseSorter<Double>(order) {
        override fun inverted(): Sorter = ScorePerGame(true)
        override val selector = UserStatsUi::scorePerGame
    }

    class MaxScoreInGame(order: Boolean = false) : BaseSorter<Int>(order) {
        override fun inverted(): Sorter = MaxScoreInGame(true)
        override val selector = UserStatsUi::maxScoreInGame
    }

    class AllGamesScore(order: Boolean = false) : BaseSorter<Int>(order) {
        override fun inverted(): Sorter = AllGamesScore(true)
        override val selector = UserStatsUi::allGamesScore
    }

    class LongestWord(order: Boolean = false) : BaseSorter<Int>(order) {
        override fun inverted(): Sorter = LongestWord(true)
        override val selector = { userStatsUi: UserStatsUi -> userStatsUi.longestWord.length }
        override fun sort(items: List<UserStatsUi>): List<UserStatsUi> {
            return if (ascendant)
                items.sortedWith(compareBy(selector).thenBy(UserStatsUi::longestWord))
            else
                items.sortedWith(compareByDescending(selector).thenBy(UserStatsUi::longestWord))
        }
    }

    class MostValuableWord(order: Boolean = true) : BaseStringSorter<String>(order) {
        override fun inverted(): Sorter = MostValuableWord(false)
        override val selector = UserStatsUi::mostValuableWord
    }

}