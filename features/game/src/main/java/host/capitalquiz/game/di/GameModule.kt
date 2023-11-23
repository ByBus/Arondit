package host.capitalquiz.game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import host.capitalquiz.core.db.mappers.GameRuleDataMapper
import host.capitalquiz.core.db.mappers.PlayerDataMapperWithParameter
import host.capitalquiz.core.db.mappers.WordDataMapper
import host.capitalquiz.game.data.BasePlayerRepository
import host.capitalquiz.game.data.GameRuleDataDataSource
import host.capitalquiz.game.data.mappers.GameRuleDataToSimpleMapper
import host.capitalquiz.game.data.mappers.PlayerDataToPlayerMapper
import host.capitalquiz.game.data.mappers.WordDataToWordMapper
import host.capitalquiz.game.domain.GameRuleInteractor
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.PlayerInteractor
import host.capitalquiz.game.domain.PlayerRepository
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.mappers.PlayerMapperWithParameter
import host.capitalquiz.game.domain.mappers.WordMapperWithParameter
import host.capitalquiz.game.ui.PlayerUi
import host.capitalquiz.game.ui.WordUi
import host.capitalquiz.game.ui.mappers.PlayerToPlayerUiMapper
import host.capitalquiz.game.ui.mappers.WordToWordUiMapper

@Module
@InstallIn(SingletonComponent::class)
interface GameModule {

    @Binds
    fun bindPlayerInteractor(impl: PlayerInteractor.Base): PlayerInteractor

    @Binds
    fun providePlayerRepository(impl: BasePlayerRepository): PlayerRepository

    @Binds
    fun bindPlayerToPlayerUiMapper(impl: PlayerToPlayerUiMapper): PlayerMapperWithParameter<GameRuleSimple, PlayerUi>

    @Binds
    fun bindWordToWordUiMapper(impl: WordToWordUiMapper): WordMapperWithParameter<GameRuleSimple, WordUi>

    @Binds
    fun bindPlayerDataMapperToPlayer(impl: PlayerDataToPlayerMapper): PlayerDataMapperWithParameter<GameRuleSimple, Player>

    @Binds
    fun bindWordDataToWordMapper(impl: WordDataToWordMapper): WordDataMapper<Word>

    @Binds
    fun bindSimpleGameInteractor(impl: GameRuleInteractor.Base): GameRuleInteractor

    @Binds
    fun bindGameRuleSimpleDataSource(impl: GameRuleDataDataSource.Base): GameRuleDataDataSource

    @Binds
    fun bindGameRuleDataMapper(impl: GameRuleDataToSimpleMapper): GameRuleDataMapper<GameRuleSimple>

}