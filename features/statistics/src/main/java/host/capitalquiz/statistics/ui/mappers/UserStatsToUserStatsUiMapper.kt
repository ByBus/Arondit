package host.capitalquiz.statistics.ui.mappers

import host.capitalquiz.statistics.domain.mappers.UserStatsMapper
import host.capitalquiz.statistics.ui.UserStatsUi
import javax.inject.Inject

class UserStatsToUserStatsUiMapper @Inject constructor() : UserStatsMapper<UserStatsUi> {
    override fun invoke(
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
    ): UserStatsUi {
        return UserStatsUi(
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