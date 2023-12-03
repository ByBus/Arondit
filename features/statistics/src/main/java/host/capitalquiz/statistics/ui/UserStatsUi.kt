package host.capitalquiz.statistics.ui

data class UserStatsUi(
    val playerName: String,
    val totalGames: Int,
    val victories: Int,
    val victoriesRate: Double,
    val words: Int,
    val wordsPerGame: Double,
    val maxWordsInGame: Int,
    val scorePerGame: Double,
    val maxScoreInGame: Int,
    val allGamesScore: Int,
    val longestWord: String,
    val mostValuableWord: String,
)
