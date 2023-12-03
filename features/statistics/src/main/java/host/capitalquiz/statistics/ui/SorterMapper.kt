package host.capitalquiz.statistics.ui

import javax.inject.Inject

interface SorterMapper<R> {
    operator fun invoke(direction: Int): R
}

interface SorterToHeaderStateMapper<R> : SorterMapper<R> {
    fun map(sorter: Sorter, headerId: Int): HeadersState

    class Base @Inject constructor() : SorterToHeaderStateMapper<HeadersState> {
        private var headerId = 0

        override fun invoke(direction: Int): HeadersState {
            return when {
                direction > 0 -> HeadersState.Ascendant(headerId)
                direction < 0 -> HeadersState.Descendant(headerId)
                else -> HeadersState.Default
            }
        }

        override fun map(sorter: Sorter, headerId: Int): HeadersState {
            this.headerId = headerId
            return sorter.map(this)
        }
    }
}