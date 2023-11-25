package host.capitalquiz.game.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import host.capitalquiz.core.db.mappers.FieldDataMapperWithParameter
import host.capitalquiz.core.db.mappers.GameRuleDataMapper
import host.capitalquiz.core.db.mappers.WordDataMapper
import host.capitalquiz.game.data.BaseFieldRepository
import host.capitalquiz.game.data.GameRuleDataDataSource
import host.capitalquiz.game.data.mappers.FieldDataToFieldMapper
import host.capitalquiz.game.data.mappers.GameRuleDataToSimpleMapper
import host.capitalquiz.game.data.mappers.WordDataToWordMapper
import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.FieldInteractor
import host.capitalquiz.game.domain.FieldRepository
import host.capitalquiz.game.domain.GameRuleInteractor
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.mappers.FieldMapperWithParameter
import host.capitalquiz.game.domain.mappers.WordMapperWithParameter
import host.capitalquiz.game.ui.FieldUi
import host.capitalquiz.game.ui.WordUi
import host.capitalquiz.game.ui.mappers.FieldToFieldUiMapper
import host.capitalquiz.game.ui.mappers.WordToWordUiMapper

@Module
@InstallIn(SingletonComponent::class)
interface GameModule {

    @Binds
    fun bindPlayerInteractor(impl: FieldInteractor.Base): FieldInteractor

    @Binds
    fun provideFieldRepository(impl: BaseFieldRepository): FieldRepository

    @Binds
    fun bindFieldToFieldUiMapper(impl: FieldToFieldUiMapper): FieldMapperWithParameter<GameRuleSimple, FieldUi>

    @Binds
    fun bindWordToWordUiMapper(impl: WordToWordUiMapper): WordMapperWithParameter<GameRuleSimple, WordUi>

    @Binds
    fun bindFieldDataToFieldMapper(impl: FieldDataToFieldMapper): FieldDataMapperWithParameter<GameRuleSimple, Field>

    @Binds
    fun bindWordDataToWordMapper(impl: WordDataToWordMapper): WordDataMapper<Word>

    @Binds
    fun bindSimpleGameInteractor(impl: GameRuleInteractor.Base): GameRuleInteractor

    @Binds
    fun bindGameRuleSimpleDataSource(impl: GameRuleDataDataSource.Base): GameRuleDataDataSource

    @Binds
    fun bindGameRuleDataMapper(impl: GameRuleDataToSimpleMapper): GameRuleDataMapper<GameRuleSimple>

}