package host.capitalquiz.statistics.ui.mappers

import host.capitalquiz.statistics.ui.HeadersUiState
import host.capitalquiz.statistics.ui.Sorter
import javax.inject.Inject

interface HeaderStateReducer : SorterMapper<HeadersUiState> {
    fun reduce(previousState: HeadersUiState, headerId: Int, sorter: Sorter): HeadersUiState

    class Base @Inject constructor() : HeaderStateReducer {
        private var headerId = 0

        override fun invoke(direction: Int): HeadersUiState {
            return when {
                direction > 0 -> HeadersUiState.Ascendant(headerId)
                direction < 0 -> HeadersUiState.Descendant(headerId)
                else -> HeadersUiState.Default
            }
        }

        override fun reduce(
            previousState: HeadersUiState,
            headerId: Int,
            sorter: Sorter,
        ): HeadersUiState {
            this.headerId = headerId
            return sorter.map(this).apply {
                rememberPrevious(previousState)
            }
        }
    }
}