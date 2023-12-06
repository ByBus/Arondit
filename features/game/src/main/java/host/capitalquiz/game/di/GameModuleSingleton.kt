package host.capitalquiz.game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import host.capitalquiz.core.db.mappers.GameRuleDataMapper
import host.capitalquiz.game.data.GameRuleDataDataSource
import host.capitalquiz.game.data.GameRuleSimpleRepository
import host.capitalquiz.game.data.mappers.GameRuleDataToSimpleMapper
import host.capitalquiz.game.domain.GameRuleRepository
import host.capitalquiz.game.domain.GameRuleSimple
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GameModuleSingleton {

    @Binds
    @Singleton
    fun bindGameRuleSimpleRepository(impl: GameRuleSimpleRepository): GameRuleRepository

    @Binds
    fun bindGameRuleSimpleDataSource(impl: GameRuleDataDataSource.Base): GameRuleDataDataSource

    @Binds
    fun bindGameRuleDataMapper(impl: GameRuleDataToSimpleMapper): GameRuleDataMapper<GameRuleSimple>
}