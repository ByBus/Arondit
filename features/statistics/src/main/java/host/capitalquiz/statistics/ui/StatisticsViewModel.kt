package host.capitalquiz.statistics.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class StatisticsViewModel : ViewModel() {

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

    fun sortByGames() = updateSorter(Sorter.TotalGames())

    fun sortByVictories() = updateSorter(Sorter.Victories())

    fun sortByAllGamesScore() = updateSorter(Sorter.AllGamesScore())

    fun sortByVictoriesPercent() = updateSorter(Sorter.VictoriesRate())

    fun sortByWordsTotal() = updateSorter(Sorter.WordsCount())

    fun sortByWordsPerGame() = updateSorter(Sorter.WordsPerGame())

    fun sortByMaxWordsInGame() = updateSorter(Sorter.MaxWordsInGame())

    fun sortByScorePerGame() = updateSorter(Sorter.ScorePerGame())

    fun sortByMaxScoreInGame() = updateSorter(Sorter.MaxScoreInGame())

    fun sortByLongestWord() = updateSorter(Sorter.LongestWord())

    fun sortByMostValuableWord() = updateSorter(Sorter.MostValuableWord())

    private fun updateSorter(sorter: Sorter) {
        this.sorter.update { oldSorter ->
            oldSorter.invertedOrThis(sorter)
        }
    }
}