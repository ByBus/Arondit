package host.capitalquiz.statistics.ui

import host.capitalquiz.statistics.databinding.TableRowItemBinding

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
    val mostValuableWordScore: Int = 0,
) {
    fun update(binding: TableRowItemBinding) {
        val item = this
        with(binding) {
            totalGames.text = item.totalGames.toString()
            victories.text = item.victories.toString()
            victoriesPercent.text = String.format("%.2f%%", item.victoriesRate)
            wordsTotal.text = item.words.toString()
            wordsPerGame.text = String.format("%.1f", item.wordsPerGame)
            maxWordsInGame.text = item.maxWordsInGame.toString()
            scorePerGame.text = String.format("%.1f", item.scorePerGame)
            maxScoreInGame.text = item.maxScoreInGame.toString()
            allGamesScore.text = item.allGamesScore.toString()
            longestWord.text = item.longestWord.takeIf { it.isNotBlank() } ?: "-"

            val valuableExist = item.mostValuableWordScore > 0
            val score = if (valuableExist) " ($mostValuableWordScore)" else ""
            val word = if (valuableExist) item.mostValuableWord else "-"
            mostValuableWord.text = "$word$score"
        }
    }
}
