package host.capitalquiz.statistics.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val sorterMapper: SorterToHeaderStateMapper<HeadersState>,
) : ViewModel() {
    private val _headersState = MutableStateFlow<HeadersState>(HeadersState.Default)
    val headersState = _headersState.asStateFlow()

    private val sorter = MutableStateFlow<Sorter>(Sorter.Default)
    private val statistics = MutableStateFlow<List<UserStatsUi>>(emptyList())

    val sortedRows = sorter.combine(statistics) { sorter, items ->
        sorter.sort(items)
    }

    init {
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

    fun sortByGames(id: Int) = updateSorter(Sorter.TotalGames(), id)

    fun sortByVictories(id: Int) = updateSorter(Sorter.Victories(), id)

    fun sortByAllGamesScore(id: Int) = updateSorter(Sorter.AllGamesScore(), id)

    fun sortByVictoriesPercent(id: Int) = updateSorter(Sorter.VictoriesRate(), id)

    fun sortByWordsTotal(id: Int) = updateSorter(Sorter.WordsCount(), id)

    fun sortByWordsPerGame(id: Int) = updateSorter(Sorter.WordsPerGame(), id)

    fun sortByMaxWordsInGame(id: Int) = updateSorter(Sorter.MaxWordsInGame(), id)

    fun sortByScorePerGame(id: Int) = updateSorter(Sorter.ScorePerGame(), id)

    fun sortByMaxScoreInGame(id: Int) = updateSorter(Sorter.MaxScoreInGame(), id)

    fun sortByLongestWord(id: Int) = updateSorter(Sorter.LongestWord(), id)

    fun sortByMostValuableWord(id: Int) = updateSorter(Sorter.MostValuableWord(), id)

    private fun updateSorter(sorter: Sorter, headerId: Int) {
        this.sorter.update { oldSorter ->
            val newSorter = oldSorter.invertedOrThis(sorter)
            _headersState.value = sorterMapper.map(newSorter, headerId)
            newSorter
        }
    }
}