package host.capitalquiz.statistics.domain

import host.capitalquiz.statistics.domain.mappers.UserStatsMapper
import java.lang.Integer.max

class UserStats(
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
    val mostValuableWordScore: Int,
) {
    operator fun plus(other: UserStats): UserStats {
        val gamesCount = this.totalGames + other.totalGames
        val victoriesCount = this.victories + other.victories
        val victoriesRate = (victoriesCount.toDouble() / gamesCount) * 100.0
        val wordsCount = this.words + other.words
        val wordsPerGame = wordsCount.toDouble() / gamesCount
        val maxWords = max(this.maxWordsInGame, other.maxWordsInGame)
        val allGamesScore = this.allGamesScore + other.allGamesScore
        val scorePerGame = allGamesScore.toDouble() / gamesCount
        val maxScoreInGame = max(this.maxScoreInGame, other.maxScoreInGame)
        val longestWord =
            if (this.longestWord.length > other.longestWord.length) this.longestWord else other.longestWord
        val mostValCompare = this.mostValuableWordScore > other.mostValuableWordScore
        val mostValuable = if (mostValCompare) this.mostValuableWord else other.mostValuableWord
        val mostValuableScore =
            if (mostValCompare) this.mostValuableWordScore else other.mostValuableWordScore
        return UserStats(
            playerName = playerName,
            totalGames = gamesCount,
            victories = victoriesCount,
            victoriesRate = victoriesRate,
            words = wordsCount,
            wordsPerGame = wordsPerGame,
            maxWordsInGame = maxWords,
            scorePerGame = scorePerGame,
            maxScoreInGame = maxScoreInGame,
            allGamesScore = allGamesScore,
            longestWord = longestWord,
            mostValuableWord = mostValuable,
            mostValuableWordScore = mostValuableScore
        )
    }

    fun <R> map(mapper: UserStatsMapper<R>): R {
        return mapper(
            playerName,
            totalGames,
            victories,
            victoriesRate,
            words,
            wordsPerGame,
            maxWordsInGame,
            scorePerGame,
            maxScoreInGame,
            allGamesScore,
            longestWord,
            mostValuableWord,
            mostValuableWordScore
        )
    }
}