package host.capitalquiz.game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import host.capitalquiz.core.db.PlayerDataMapper
import host.capitalquiz.core.db.WordDataMapper
import host.capitalquiz.game.data.BasePlayerRepository
import host.capitalquiz.game.data.PlayerDataMapperToPlayer
import host.capitalquiz.game.data.StringFormatter
import host.capitalquiz.game.data.WordDataToWordMapper
import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.PlayerInteractor
import host.capitalquiz.game.domain.PlayerMapper
import host.capitalquiz.game.domain.PlayerRepository
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.WordMapper
import host.capitalquiz.game.ui.PlayerToPlayerUiMapper
import host.capitalquiz.game.ui.PlayerUi
import host.capitalquiz.game.ui.WordToWordUiMapper
import host.capitalquiz.game.ui.WordUi

@Module
@InstallIn(SingletonComponent::class)
abstract class GameModule {

    @Binds
    abstract fun bindPlayerInteractor(impl: PlayerInteractor.Base): PlayerInteractor

    @Binds
    abstract fun providePlayerRepository(impl: BasePlayerRepository): PlayerRepository

    @Binds
    abstract fun bindPlayerToPlayerUiMapper(impl: PlayerToPlayerUiMapper): PlayerMapper<PlayerUi>

    @Binds
    abstract fun bindWordToWordUiMapper(impl: WordToWordUiMapper): WordMapper<WordUi>

    @Binds
    abstract fun bindPlayerDataMapperToPlayer(impl: PlayerDataMapperToPlayer): PlayerDataMapper<Player>

    @Binds
    abstract fun bindWordDataToWordMapper(impl: WordDataToWordMapper): WordDataMapper<Word>

    @Binds
    abstract fun bindStringFormatter(impl: StringFormatter.UnwantedCharToAsteriskReplacer): StringFormatter
}