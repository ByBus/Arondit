package host.capitalquiz.arondit.game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.arondit.core.db.PlayerDataMapper
import host.capitalquiz.arondit.core.db.WordDataMapper
import host.capitalquiz.arondit.game.data.BasePlayerRepository
import host.capitalquiz.arondit.game.data.PlayerDataMapperToPlayer
import host.capitalquiz.arondit.game.data.StringFormatter
import host.capitalquiz.arondit.game.data.WordDataToWordMapper
import host.capitalquiz.arondit.game.domain.Player
import host.capitalquiz.arondit.game.domain.PlayerInteractor
import host.capitalquiz.arondit.game.domain.PlayerMapper
import host.capitalquiz.arondit.game.domain.PlayerRepository
import host.capitalquiz.arondit.game.domain.Word
import host.capitalquiz.arondit.game.domain.WordMapper
import host.capitalquiz.arondit.game.ui.PlayerToPlayerUiMapper
import host.capitalquiz.arondit.game.ui.PlayerUi
import host.capitalquiz.arondit.game.ui.WordToWordUiMapper
import host.capitalquiz.arondit.game.ui.WordUi

@Module
@InstallIn(ViewModelComponent::class)
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