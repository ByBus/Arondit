package host.capitalquiz.statistics.domain

import javax.inject.Inject

interface StatisticsInteractor {

    suspend fun allUserStatistics(): List<UserGameShortStats>

    class Base @Inject constructor(
        private val statistRepository: UserStatsRepository,
    ) : StatisticsInteractor {
        override suspend fun allUserStatistics(): List<UserGameShortStats> {
            return statistRepository.getAllUsersStats()
        }
    }
}