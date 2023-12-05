package host.capitalquiz.statistics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.statistics.data.BaseUserStatsRepository
import host.capitalquiz.statistics.data.mappers.GameDataToUserGameShortStatsMapper
import host.capitalquiz.statistics.data.mappers.UserStatisticsProducer
import host.capitalquiz.statistics.domain.StatisticsFormatter
import host.capitalquiz.statistics.domain.StatisticsInteractor
import host.capitalquiz.statistics.domain.UserStats
import host.capitalquiz.statistics.domain.UserStatsRepository
import host.capitalquiz.statistics.domain.mappers.UserGameShortStatsMapper
import host.capitalquiz.statistics.domain.mappers.UserStatsMapper
import host.capitalquiz.statistics.ui.UserStatsUi
import host.capitalquiz.statistics.ui.mappers.HeaderStateReducer
import host.capitalquiz.statistics.ui.mappers.UserStatsToUserStatsUiMapper

@Module
@InstallIn(ViewModelComponent::class)
interface StatisticsModule {

    @Binds
    fun bindUserStatsRepository(impl: BaseUserStatsRepository): UserStatsRepository

    @Binds
    fun bindGameDataToUserGameShortStatsMapper(impl: GameDataToUserGameShortStatsMapper): UserStatisticsProducer

    @Binds
    fun bindStatisticsInteractor(impl: StatisticsInteractor.Base): StatisticsInteractor

    @Binds
    fun bindHeaderStateReducer(impl: HeaderStateReducer.Base): HeaderStateReducer

    @Binds
    fun bindUserGameShortStatsMapper(impl: UserGameShortStatsMapper.ToUserStats): UserGameShortStatsMapper<UserStats>

    @Binds
    fun bindStatisticsFormatter(impl: StatisticsFormatter.Base): StatisticsFormatter

    @Binds
    fun bindUserStatsMapper(impl: UserStatsToUserStatsUiMapper): UserStatsMapper<UserStatsUi>
}