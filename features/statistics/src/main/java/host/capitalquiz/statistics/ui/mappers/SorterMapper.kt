package host.capitalquiz.statistics.ui.mappers

interface SorterMapper<R> {
    operator fun invoke(direction: Int): R
}