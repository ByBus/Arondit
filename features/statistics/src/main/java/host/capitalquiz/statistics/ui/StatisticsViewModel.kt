package host.capitalquiz.statistics.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.statistics.domain.StatisticsInteractor
import host.capitalquiz.statistics.ui.mappers.HeaderStateReducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val interactor: StatisticsInteractor,
    private val headersReducer: HeaderStateReducer,
) : ViewModel() {
    private var lastClickedHeaderId = 0
    private val sorter = MutableStateFlow<Sorter>(Sorter.Default)
    private val statistics = MutableStateFlow<List<UserStatsUi>>(emptyList())

    private val _headersState = MutableStateFlow<HeadersState>(HeadersState.Default)
    val headersState = _headersState.asStateFlow()

    val sortedRows = sorter.combine(statistics) { sorter, items ->
        sorter.sort(items)
    }

    init {
        viewModelScope.launch {
            sorter.collectLatest { sorter ->
                _headersState.update { oldState ->
                    headersReducer.reduce(oldState, lastClickedHeaderId, sorter)
                }
            }
        }
        val words = listOf(
            "недопуск",
            "бронотозавр",
            "артель",
            "невзрачность",
            "чёрствость",
            "назойливость",
            "выхоливание",
            "звероловство",
            "зыбун",
            "унтертон",
            "оскал",
            "нахвостник"
        ).map { it.uppercase() }
        val items = List(100) {
            UserStatsUi(
                "Player $it",
                (1..20).random(),
                (0..10).random(),
                Random.nextDouble() * 100,
                (10..200).random(),
                Random.nextDouble() * 15,
                (10..20).random(),
                Random.nextDouble() * 100,
                (200..300).random(),
                (400..800).random(),
                words.random(),
                "СИНХРОФАЗОТРОН 50"
            )
        }
        statistics.update {
            items
        }
    }

    fun sortByGames(id: Int) {
        updateState(Sorter.TotalGames(), id)
    }

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