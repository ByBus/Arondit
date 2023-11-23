package host.capitalquiz.editgamerule.ui.editscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import host.capitalquiz.editgamerule.domain.EditGameRuleInteractor
import host.capitalquiz.editgamerule.domain.GameRule
import host.capitalquiz.editgamerule.domain.GameRuleMapper
import host.capitalquiz.editgamerule.ui.editscreen.mappers.EditableGameRuleUiMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val EDIT_RULE = -1L

class EditGameRuleViewModel @AssistedInject constructor(
    private val ruleInteractor: EditGameRuleInteractor,
    private val ruleToUiMapper: GameRuleMapper<EditableGameRuleUi>,
    private val ruleUiToRuleMapper: EditableGameRuleUiMapper<GameRule>,
    @Assisted private val ruleId: Long,
) : ViewModel() {
    private val latestRuleId = MutableStateFlow(ruleId)
    private val _editLetterNavigation = Channel<NavigationEvent>()
    val editLetterNavigation = _editLetterNavigation.receiveAsFlow()

    val gameRule = latestRuleId.flatMapLatest { id ->
        if (id == EDIT_RULE)
            flow { EditableGameRuleUi.empty() }
        else
            ruleInteractor.getGameRule(id)
    }.map { gameRule ->
        gameRule.map(ruleToUiMapper)
    }

    fun createNewRule(name: String) {
        viewModelScope.launch {
            latestRuleId.update { ruleInteractor.createNewRule(name) }
        }
    }

    fun updateRule(ruleId: Long) {
        latestRuleId.update { ruleId }
    }

    fun renameRule(newName: String) {
        viewModelScope.launch {
            ruleInteractor.renameRule(newName, gameRule.first().map(ruleUiToRuleMapper))
        }
    }

    fun navigateToEditLetter(letter: Char? = null, points: Int = -1){
        _editLetterNavigation.trySend(NavigationEvent(latestRuleId.value, letter, points))
    }

    companion object {
        fun factory(
            assistedFactory: EditGameRuleViewModelFactory,
            ruleId: Long,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(ruleId) as T
            }
        }
    }
}


@AssistedFactory
interface EditGameRuleViewModelFactory {
    fun create(ruleId: Long): EditGameRuleViewModel
}
