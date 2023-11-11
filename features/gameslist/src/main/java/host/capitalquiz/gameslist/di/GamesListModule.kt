package host.capitalquiz.gameslist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.core.datastore.SettingsLocalDataSource
import host.capitalquiz.core.datastore.SettingsReadDataSource
import host.capitalquiz.core.db.GameDataMapper
import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.PlayerDataMapperWithParameter
import host.capitalquiz.core.db.WordDataMapperWithParameter
import host.capitalquiz.gameslist.data.BaseSettingsRepository
import host.capitalquiz.gameslist.data.GameDataToGameMapper
import host.capitalquiz.gameslist.data.GamesListRepository
import host.capitalquiz.gameslist.data.PlayerDataToPlayerInfoMapper
import host.capitalquiz.gameslist.domain.Game
import host.capitalquiz.gameslist.domain.GameMapper
import host.capitalquiz.gameslist.domain.GamesListInteractor
import host.capitalquiz.gameslist.domain.GamesRepository
import host.capitalquiz.gameslist.domain.PlayerInfo
import host.capitalquiz.gameslist.domain.SettingsReadRepository
import host.capitalquiz.gameslist.domain.WordDataToScoreMapper
import host.capitalquiz.gameslist.ui.GameToGameUiMapper
import host.capitalquiz.gameslist.ui.GameUi

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
    fun bindPlayerDataToPlayerInfoMapper(impl: PlayerDataToPlayerInfoMapper): PlayerDataMapperWithParameter<GameRuleData, PlayerInfo>

    @Binds
    fun bindWordDataToScoreMapper(impl: WordDataToScoreMapper): WordDataMapperWithParameter<Map<Char, Int>, Int>

    @Binds
    fun bindGameToGameUiMapper(impl: GameToGameUiMapper): GameMapper<GameUi>

    @Binds
    fun bindSettingsRepository(impl: BaseSettingsRepository): SettingsReadRepository

    @Binds
    fun bindSettingsReadOnlyDataSource(impl: SettingsLocalDataSource.BaseDataStore): SettingsReadDataSource

}