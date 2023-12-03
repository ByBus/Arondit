package host.capitalquiz.statistics.domain

data class UserGameShortStats(
    val playerId: Long,
    val playerName: String,
    val longestWord: String,
    val mostValuableWord: String,
    val mostValuableWordScore: Int,
    val wordsCount: Int,
    val score: Int,
    val victory: Boolean,
)
