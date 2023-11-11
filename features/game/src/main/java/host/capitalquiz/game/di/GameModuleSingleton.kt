package host.capitalquiz.game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import host.capitalquiz.game.data.GameRuleSimpleRepository
import host.capitalquiz.game.domain.GameRuleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GameModuleSingleton {

    @Binds
    @Singleton
    fun bindGameRuleSimpleRepository(impl: GameRuleSimpleRepository) : GameRuleRepository
}