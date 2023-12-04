package host.capitalquiz.statistics.ui

interface SorterMapper<R> {
    operator fun invoke(direction: Int): R
}