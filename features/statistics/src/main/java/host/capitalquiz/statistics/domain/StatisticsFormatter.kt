package host.capitalquiz.statistics.domain

import host.capitalquiz.statistics.domain.mappers.UserGameShortStatsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface StatisticsFormatter {
    suspend fun format(data: List<UserGameShortStats>): List<UserStats>

    class Base @Inject constructor(
        private val userStatsMapper: UserGameShortStatsMapper<UserStats>,
    ) : StatisticsFormatter {
        override suspend fun format(data: List<UserGameShortStats>): List<UserStats> {
            return withContext(Dispatchers.Default) {
                val groupedStats = data
                    .groupBy(keySelector = { it.playerId }) { shortStats ->
                        shortStats.map(userStatsMapper)
                    }

                groupedStats.values.reduce { acc, userStats ->
                    acc + userStats
                }.sortedBy { it.playerName }
            }
        }
    }
}