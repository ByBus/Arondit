package host.capitalquiz.editgamerule.ui.editscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import host.capitalquiz.editgamerule.domain.EditGameRuleInteractor
import host.capitalquiz.editgamerule.domain.GameRuleMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val EDIT_RULE = -1L

class EditGameRuleViewModel @AssistedInject constructor(
    private val ruleInteractor: EditGameRuleInteractor,
    private val ruleToUiMapper: GameRuleMapper<EditableGameRuleUi>,
    @Assisted private var ruleId: Long,
) : ViewModel() {
    private var rule = MutableStateFlow(EditableGameRuleUi(0L, "", false, emptyList()))
    val gameRule = rule.asLiveData()

    init {
        if (ruleId != EDIT_RULE) viewModelScope.launch { loadGame() }
    }

    private suspend fun loadGame() {
        ruleInteractor.getGameRule(ruleId).map {
            it.map(ruleToUiMapper)
        }.collect {
            rule.value = it
        }
    }

    fun createNewRule(name: String) {
        viewModelScope.launch {
            ruleId = ruleInteractor.createNewRule(name)
            loadGame()
        }
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
