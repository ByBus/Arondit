package host.capitalquiz.statistics.ui

import javax.inject.Inject

interface HeaderStateReducer : SorterMapper<HeadersState> {
    fun reduce(previousState: HeadersState, headerId: Int, sorter: Sorter): HeadersState

    class Base @Inject constructor() : HeaderStateReducer {
        private var headerId = 0

        override fun invoke(direction: Int): HeadersState {
            return when {
                direction > 0 -> HeadersState.Ascendant(headerId)
                direction < 0 -> HeadersState.Descendant(headerId)
                else -> HeadersState.Default
            }
        }

        override fun reduce(
            previousState: HeadersState,
            headerId: Int,
            sorter: Sorter,
        ): HeadersState {
            this.headerId = headerId
            return sorter.map(this).apply {
                rememberPrevious(previousState)
            }
        }
    }
}