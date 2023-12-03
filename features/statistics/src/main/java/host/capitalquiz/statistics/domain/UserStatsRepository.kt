package host.capitalquiz.statistics.domain


interface UserStatsRepository {
    suspend fun getAllUsersStats(): List<UserGameShortStats>
}