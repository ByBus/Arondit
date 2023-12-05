package host.capitalquiz.statistics.domain.mappers

interface UserStatsMapper<R> {
    operator fun invoke(
        playerName: String,
        totalGames: Int,
        victories: Int,
        victoriesRate: Double,
        words: Int,
        wordsPerGame: Double,
        maxWordsInGame: Int,
        scorePerGame: Double,
        maxScoreInGame: Int,
        allGamesScore: Int,
        longestWord: String,
        mostValuableWord: String,
        mostValuableWordScore: Int,
    ): R
}