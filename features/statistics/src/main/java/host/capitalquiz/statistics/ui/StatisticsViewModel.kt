package host.capitalquiz.statistics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.statistics.domain.StatisticsInteractor
import host.capitalquiz.statistics.domain.mappers.UserStatsMapper
import host.capitalquiz.statistics.ui.mappers.HeaderStateReducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val interactor: StatisticsInteractor,
    private val headersReducer: HeaderStateReducer,
    private val statsMapper: UserStatsMapper<UserStatsUi>,
) : ViewModel() {
    private var lastClickedHeaderId = 0
    private val sorter = MutableStateFlow<Sorter>(Sorter.Default)
    private val statistics = MutableStateFlow<List<UserStatsUi>>(emptyList())

    private val _headersState = MutableStateFlow<HeadersState>(HeadersState.Default)
    val headersState = _headersState.asStateFlow()

    val sortedRows = sorter.combine(statistics) { sorter, items ->
        sorter.sort(items)
    }

    val showInformation = statistics.map { stats ->
        stats.all { it.allGamesScore == 0 }
    }

    init {
        viewModelScope.launch {
            sorter.collectLatest { sorter ->
                _headersState.update { oldState ->
                    headersReducer.reduce(oldState, lastClickedHeaderId, sorter)
                }
            }
        }
    }

    fun loadStatistics() {
        viewModelScope.launch {
            statistics.update {
                interactor.allUserStatistics().map {
                    it.map(statsMapper)
                }
            }
        }
    }

    fun sortByGames(id: Int) = updateState(Sorter.TotalGames(), id)

    fun sortByVictories(id: Int) = updateState(Sorter.Victories(), id)

    fun sortByAllGamesScore(id: Int) = updateState(Sorter.AllGamesScore(), id)

    fun sortByVictoriesPercent(id: Int) = updateState(Sorter.VictoriesRate(), id)

    fun sortByWordsTotal(id: Int) = updateState(Sorter.WordsCount(), id)

    fun sortByWordsPerGame(id: Int) = updateState(Sorter.WordsPerGame(), id)

    fun sortByMaxWordsInGame(id: Int) = updateState(Sorter.MaxWordsInGame(), id)

    fun sortByScorePerGame(id: Int) = updateState(Sorter.ScorePerGame(), id)

    fun sortByMaxScoreInGame(id: Int) = updateState(Sorter.MaxScoreInGame(), id)

    fun sortByLongestWord(id: Int) = updateState(Sorter.LongestWord(), id)

    fun sortByMostValuableWord(id: Int) = updateState(Sorter.MostValuableWord(), id)

    private fun updateState(sorter: Sorter, headerId: Int) {
        lastClickedHeaderId = headerId
        updateSorter(sorter)
    }

    private fun updateSorter(sorter: Sorter) {
        this.sorter.update { oldSorter ->
            oldSorter.invertedOrThis(sorter)
        }
    }
}