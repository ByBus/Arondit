package host.capitalquiz.editgamerule.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.mappers.GameRuleDataMapper
import host.capitalquiz.editgamerule.data.BaseEditGameRuleRepository
import host.capitalquiz.editgamerule.data.mappers.GameRuleDataToGameRuleMapper
import host.capitalquiz.editgamerule.data.mappers.GameRuleToGameRuleDataMapper
import host.capitalquiz.editgamerule.domain.CharProvider
import host.capitalquiz.editgamerule.domain.EditGameRuleInteractor
import host.capitalquiz.editgamerule.domain.EditGameRuleRepository
import host.capitalquiz.editgamerule.domain.GameRule
import host.capitalquiz.editgamerule.domain.GameRuleMapper
import host.capitalquiz.editgamerule.domain.LetterAddResultMapper
import host.capitalquiz.editgamerule.domain.LetterAddResultMapperWithParameter
import host.capitalquiz.editgamerule.ui.dialog.AddLetterUiState
import host.capitalquiz.editgamerule.ui.dialog.mappers.CreateLetterAddResultToUiMapper
import host.capitalquiz.editgamerule.ui.dialog.mappers.EditLetterAddResultToUiMapper
import host.capitalquiz.editgamerule.ui.editscreen.EditableGameRuleUi
import host.capitalquiz.editgamerule.ui.editscreen.mappers.EditableGameRuleUiMapper
import host.capitalquiz.editgamerule.ui.editscreen.mappers.EditableRuleToUiMapper

@Module
@InstallIn(ViewModelComponent::class)
interface EditGameRuleModule {

    @Binds
    fun bindGameRuleDataToGameRuleMapper(impl: GameRuleDataToGameRuleMapper): GameRuleDataMapper<GameRule>

    @Binds
    fun bindEditGameRuleRepository(impl: BaseEditGameRuleRepository): EditGameRuleRepository

    @Binds
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
    @CreationMode
    fun bindLetterAddResultMapperCreateMode(impl: CreateLetterAddResultToUiMapper): LetterAddResultMapperWithParameter<Boolean, AddLetterUiState>

    @Binds
    @EditMode
    fun bindLetterAddResultMapperEditMode(impl: EditLetterAddResultToUiMapper): LetterAddResultMapper<AddLetterUiState>
}