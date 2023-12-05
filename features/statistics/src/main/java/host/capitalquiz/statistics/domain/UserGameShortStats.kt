package host.capitalquiz.statistics.domain

import host.capitalquiz.statistics.domain.mappers.UserGameShortStatsMapper

data class UserGameShortStats(
    val playerId: Long,
    val playerName: String,
    val longestWord: String,
    val mostValuableWord: String,
    val mostValuableWordScore: Int,
    val wordsCount: Int,
    val score: Int,
    val victory: Boolean,
) {
    fun <R> map(mapper: UserGameShortStatsMapper<R>): R {
        return mapper(
            playerId,
            playerName,
            longestWord,
            mostValuableWord,
            mostValuableWordScore,
            wordsCount,
            score,
            victory
        )
    }
}
