package host.capitalquiz.statistics.domain

import javax.inject.Inject

interface StatisticsInteractor {

    suspend fun allUserStatistics(): List<UserStats>

    class Base @Inject constructor(
        private val statistRepository: UserStatsRepository,
        private val statisticsFormatter: StatisticsFormatter,
    ) : StatisticsInteractor {
        override suspend fun allUserStatistics(): List<UserStats> {
            val allUsersStats = statistRepository.getAllUsersStats()
            return statisticsFormatter.format(allUsersStats)
        }
    }
}