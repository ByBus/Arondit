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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val EDIT_RULE = -1L

class EditGameRuleViewModel @AssistedInject constructor(
    private val ruleInteractor: EditGameRuleInteractor,
    private val ruleToUiMapper: GameRuleMapper<EditableGameRuleUi>,
    private val ruleUiToRuleMapper: EditableGameRuleUiMapper<GameRule>,
    @Assisted private val ruleId: Long,
) : ViewModel() {
    private val latestRuleId = MutableStateFlow(ruleId)
    private var ruleCopyNamePrefix: String = ""

    val gameRule = latestRuleId.flatMapLatest { id ->
        if (id == EDIT_RULE)
            flow { EditableGameRuleUi.empty() }
        else
            ruleInteractor.getGameRule(id)
    }.map { gameRule ->
        gameRule.map(ruleToUiMapper)
    }

    fun init(ruleCopyNamePrefix: String) {
        this.ruleCopyNamePrefix = ruleCopyNamePrefix
    }

    fun createNewRule(name: String) {
        viewModelScope.launch {
            latestRuleId.value = ruleInteractor.createNewRule(name)
        }
    }

    fun saveLetter(letter: Char, points: Int) {
        viewModelScope.launch {
            val rule = gameRule.first()
            if (rule.readOnly) {
                latestRuleId.value = ruleInteractor.createCopyOfRule(
                    ruleCopyNamePrefix,
                    rule.map(ruleUiToRuleMapper)
                )
            }
            ruleInteractor.addLetterToRule(letter, points, latestRuleId.value)
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
