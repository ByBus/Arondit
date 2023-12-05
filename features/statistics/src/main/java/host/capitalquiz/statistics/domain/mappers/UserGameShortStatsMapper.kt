package host.capitalquiz.statistics.domain.mappers

import host.capitalquiz.statistics.domain.UserStats
import javax.inject.Inject

interface UserGameShortStatsMapper<R> {
    operator fun invoke(
        playerId: Long,
        playerName: String,
        longestWord: String,
        mostValuableWord: String,
        mostValuableWordScore: Int,
        wordsCount: Int,
        score: Int,
        victory: Boolean,
    ): R

    class ToUserStats @Inject constructor() : UserGameShortStatsMapper<UserStats> {
        override fun invoke(
            playerId: Long,
            playerName: String,
            longestWord: String,
            mostValuableWord: String,
            mostValuableWordScore: Int,
            wordsCount: Int,
            score: Int,
            victory: Boolean,
        ): UserStats {
            return UserStats(
                playerName = playerName,
                totalGames = 1,
                victories = if (victory) 1 else 0,
                victoriesRate = if (victory) 100.0 else 0.0,
                words = wordsCount,
                wordsPerGame = wordsCount.toDouble(),
                maxWordsInGame = wordsCount,
                scorePerGame = score.toDouble(),
                maxScoreInGame = score,
                allGamesScore = score,
                longestWord = longestWord,
                mostValuableWord = mostValuableWord,
                mostValuableWordScore = mostValuableWordScore
            )
        }

    }
}