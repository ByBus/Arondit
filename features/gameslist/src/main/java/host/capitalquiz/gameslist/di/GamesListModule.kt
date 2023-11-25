package host.capitalquiz.gameslist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.core.datastore.SettingsLocalDataSource
import host.capitalquiz.core.datastore.SettingsReadDataSource
import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.mappers.FieldDataMapperWithParameter
import host.capitalquiz.core.db.mappers.GameDataMapper
import host.capitalquiz.core.db.mappers.WordDataMapperWithParameter
import host.capitalquiz.gameslist.data.BaseSettingsRepository
import host.capitalquiz.gameslist.data.GamesListRepository
import host.capitalquiz.gameslist.data.mappers.FieldDataToPlayerInfoMapper
import host.capitalquiz.gameslist.data.mappers.GameDataToGameMapper
import host.capitalquiz.gameslist.domain.Game
import host.capitalquiz.gameslist.domain.GamesListInteractor
import host.capitalquiz.gameslist.domain.GamesRepository
import host.capitalquiz.gameslist.domain.PlayerInfo
import host.capitalquiz.gameslist.domain.SettingsReadRepository
import host.capitalquiz.gameslist.domain.mappers.GameMapper
import host.capitalquiz.gameslist.domain.mappers.WordDataToScoreMapper
import host.capitalquiz.gameslist.ui.GameUi
import host.capitalquiz.gameslist.ui.mappers.GameToGameUiMapper

@Module
@InstallIn(ViewModelComponent::class)
interface GamesListModule {

    @Binds
    fun bindGamesListInteractor(impl: GamesListInteractor.Base): GamesListInteractor

    @Binds
    fun bindGamesListRepository(impl: GamesListRepository): GamesRepository

    @Binds
    fun bindGameDataToGameMapper(impl: GameDataToGameMapper): GameDataMapper<Game>

    @Binds
    fun bindFieldDataToPlayerInfoMapper(impl: FieldDataToPlayerInfoMapper): FieldDataMapperWithParameter<GameRuleData, PlayerInfo>

    @Binds
    fun bindWordDataToScoreMapper(impl: WordDataToScoreMapper): WordDataMapperWithParameter<Map<Char, Int>, Int>

    @Binds
    fun bindGameToGameUiMapper(impl: GameToGameUiMapper): GameMapper<GameUi>

    @Binds
    fun bindSettingsRepository(impl: BaseSettingsRepository): SettingsReadRepository

    @Binds
    fun bindSettingsReadOnlyDataSource(impl: SettingsLocalDataSource.BaseDataStore): SettingsReadDataSource

}