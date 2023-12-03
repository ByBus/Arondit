package host.capitalquiz.statistics.data

import host.capitalquiz.core.db.GameDao
import host.capitalquiz.statistics.data.mappers.UserStatisticsProducer
import host.capitalquiz.statistics.domain.UserGameShortStats
import host.capitalquiz.statistics.domain.UserStatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BaseUserStatsRepository @Inject constructor(
    private val gameDao: GameDao,
    private val statisticsMapper: UserStatisticsProducer,
) : UserStatsRepository {
    override suspend fun getAllUsersStats(): List<UserGameShortStats> {
        return withContext(Dispatchers.IO) {
            val allGames = gameDao.allGames().first()
            allGames.flatMap { gameWithPlayers ->
                gameWithPlayers.map(statisticsMapper)
            }
        }
    }
}