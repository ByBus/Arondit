package host.capitalquiz.statistics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.statistics.data.BaseUserStatsRepository
import host.capitalquiz.statistics.data.mappers.GameDataToUserGameShortStatsMapper
import host.capitalquiz.statistics.data.mappers.UserStatisticsProducer
import host.capitalquiz.statistics.domain.StatisticsInteractor
import host.capitalquiz.statistics.domain.UserStatsRepository
import host.capitalquiz.statistics.ui.HeadersState
import host.capitalquiz.statistics.ui.SorterToHeaderStateMapper

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
    fun bindSorterToHeadersStateMapper(impl: SorterToHeaderStateMapper.Base): SorterToHeaderStateMapper<HeadersState>
}