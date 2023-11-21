package host.capitalquiz.editgamerule.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.GameRuleDataMapper
import host.capitalquiz.editgamerule.data.BaseEditGameRuleRepository
import host.capitalquiz.editgamerule.data.GameRuleDataToGameRuleMapper
import host.capitalquiz.editgamerule.data.GameRuleToGameRuleDataMapper
import host.capitalquiz.editgamerule.domain.CharProvider
import host.capitalquiz.editgamerule.domain.EditGameRuleInteractor
import host.capitalquiz.editgamerule.domain.EditGameRuleRepository
import host.capitalquiz.editgamerule.domain.GameRule
import host.capitalquiz.editgamerule.domain.GameRuleMapper
import host.capitalquiz.editgamerule.domain.LetterAddResultMapperWithParameter
import host.capitalquiz.editgamerule.ui.dialog.AddLetterUiState
import host.capitalquiz.editgamerule.ui.dialog.LetterAddResultToUiMapper
import host.capitalquiz.editgamerule.ui.editscreen.EditableGameRuleUi
import host.capitalquiz.editgamerule.ui.editscreen.EditableGameRuleUiMapper
import host.capitalquiz.editgamerule.ui.editscreen.EditableRuleToUiMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface EditGameRuleModule {

    @Binds
    fun bindGameRuleDataToGameRuleMapper(impl: GameRuleDataToGameRuleMapper): GameRuleDataMapper<GameRule>

    @Binds
    fun bindEditGameRuleRepository(impl: BaseEditGameRuleRepository): EditGameRuleRepository

    @Binds
    @Singleton
    fun bindEditGameRuleInteractor(impl: EditGameRuleInteractor.Base): EditGameRuleInteractor

    @Binds
    fun bindGameRuleToEditableRuleMapper(impl: EditableRuleToUiMapper): GameRuleMapper<EditableGameRuleUi>

    @Binds
    fun bindGameRuleToGameRuleDataMapper(impl: GameRuleToGameRuleDataMapper) : GameRuleMapper<GameRuleData>

    @Binds
    fun bindEditableGameRuleUiMapper(impl: EditableGameRuleUiMapper.ToGameRule): EditableGameRuleUiMapper<GameRule>


    @Binds
    fun bindNextCharPredictor(impl: CharProvider.NextChar): CharProvider

    @Binds
    fun bindLetterAddResultMapper(impl: LetterAddResultToUiMapper): LetterAddResultMapperWithParameter<Boolean, AddLetterUiState>
}