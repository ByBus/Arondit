package host.capitalquiz.arondit.gameslist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.arondit.core.db.GameDataMapper
import host.capitalquiz.arondit.core.db.PlayerDataMapper
import host.capitalquiz.arondit.core.db.WordDataMapper
import host.capitalquiz.arondit.gameslist.data.GameDataToGameMapper
import host.capitalquiz.arondit.gameslist.data.GamesListRepository
import host.capitalquiz.arondit.gameslist.data.PlayerDataToPlayerInfoMapper
import host.capitalquiz.arondit.gameslist.domain.Game
import host.capitalquiz.arondit.gameslist.domain.GameMapper
import host.capitalquiz.arondit.gameslist.domain.GamesListInteractor
import host.capitalquiz.arondit.gameslist.domain.GamesRepository
import host.capitalquiz.arondit.gameslist.domain.PlayerInfo
import host.capitalquiz.arondit.gameslist.domain.WordDataToScoreMapper
import host.capitalquiz.arondit.gameslist.ui.GameToGameUiMapper
import host.capitalquiz.arondit.gameslist.ui.GameUi

@Module
@InstallIn(ViewModelComponent::class)
abstract class GamesListModule {

    @Binds
    abstract fun bindGamesListInteractor(impl: GamesListInteractor.Base): GamesListInteractor

    @Binds
    abstract fun bindGamesListRepository(impl: GamesListRepository): GamesRepository

    @Binds
    abstract fun bindGameDataToGameMapper(impl: GameDataToGameMapper): GameDataMapper<Game>

    @Binds
    abstract fun bindPlayerDataToPlayerInfoMapper(impl: PlayerDataToPlayerInfoMapper): PlayerDataMapper<PlayerInfo>

    @Binds
    abstract fun bindWordDataToScoreMapper(impl: WordDataToScoreMapper): WordDataMapper<Int>

    @Binds
    abstract fun bindGameToGameUiMapper(impl: GameToGameUiMapper): GameMapper<GameUi>

}