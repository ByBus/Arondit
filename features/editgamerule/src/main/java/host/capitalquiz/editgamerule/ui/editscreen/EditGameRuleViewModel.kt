package host.capitalquiz.editgamerule.ui.editscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel(assistedFactory = EditGameRuleViewModelFactory::class)
class EditGameRuleViewModel @AssistedInject constructor(
    private val ruleInteractor: EditGameRuleInteractor,
    private val ruleToUiMapper: GameRuleMapper<EditableGameRuleUi>,
    private val ruleUiToRuleMapper: EditableGameRuleUiMapper<GameRule>,
    @Assisted private val ruleId: Long,
) : ViewModel() {
    private val latestRuleId = MutableStateFlow(ruleId)
    private val navigationChannel = Channel<NavigationEvent>()
    val navigationEvent = navigationChannel.receiveAsFlow()

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

    fun goToAddLetterDialog(letter: Char? = null, points: Int = -1) {
        navigationChannel.trySend(
            NavigationEvent.AddLetterDialog(
                latestRuleId.value,
                letter,
                points
            )
        )
    }

    fun goBack() {
        navigationChannel.trySend(NavigationEvent.Up)
    }

    fun goToRenameRuleDialog() {
        viewModelScope.launch {
            navigationChannel.trySend(NavigationEvent.RenameRuleDialog(gameRule.first().name))
        }
    }
}


@AssistedFactory
interface EditGameRuleViewModelFactory {
    fun create(ruleId: Long): EditGameRuleViewModel
}
