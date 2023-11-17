package host.capitalquiz.editgamerule.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import host.capitalquiz.core.db.GameRuleWithGamesMapper
import host.capitalquiz.editgamerule.data.BaseGameRuleRepository
import host.capitalquiz.editgamerule.data.GameDataSource
import host.capitalquiz.editgamerule.data.GameRuleDataSource
import host.capitalquiz.editgamerule.data.GameRuleWithGamesDataToGameRuleMapper
import host.capitalquiz.editgamerule.domain.GameRule
import host.capitalquiz.editgamerule.domain.GameRuleInteractor
import host.capitalquiz.editgamerule.domain.GameRuleRepository

@Module
@InstallIn(SingletonComponent::class)
interface GameRuleModule {

    @Binds
    fun bindGameRuleDataSource(impl: GameRuleDataSource.Base): GameRuleDataSource

    @Binds
    fun bindGameRuleWithGamesMapper(impl: GameRuleWithGamesDataToGameRuleMapper): GameRuleWithGamesMapper<GameRule>

    @Binds
    fun bindGameRuleRepository(impl: BaseGameRuleRepository): GameRuleRepository

    @Binds
    fun bindGameRuleInteractor(impl: GameRuleInteractor.Base): GameRuleInteractor

    @Binds
    fun bindGameDataSource(impl: GameDataSource.Base): GameDataSource

}