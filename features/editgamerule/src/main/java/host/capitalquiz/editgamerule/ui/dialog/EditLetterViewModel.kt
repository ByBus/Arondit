package host.capitalquiz.editgamerule.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import host.capitalquiz.editgamerule.domain.EditGameRuleInteractor
import host.capitalquiz.editgamerule.domain.LetterAddResultMapperWithParameter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditLetterViewModel @AssistedInject constructor(
    private val ruleInteractor: EditGameRuleInteractor,
    private val uiStateMapper: LetterAddResultMapperWithParameter<Boolean, AddLetterUiState>,
    @Assisted private val ruleId: Long,
) : ViewModel() {
    private var ruleNamePrefix: String = ""
    private val _latestRuleId = MutableStateFlow(ruleId)
    val latestRuleId = _latestRuleId.asStateFlow()

    private var _addLetterUiState = MutableStateFlow<AddLetterUiState>(AddLetterUiState.Init)
    val addLetterUiState = _addLetterUiState.asStateFlow()

    fun init(ruleCopyNamePrefix: String) {
        ruleNamePrefix = ruleCopyNamePrefix
    }

    fun initLetter(letter: Char, point: Int) {
        _addLetterUiState.update {
            AddLetterUiState.NextLetter(letter, point)
        }
    }

    fun saveLetter(letter: Char, points: Int, closeAfter: Boolean, replace: Boolean = false) {
        viewModelScope.launch {
            val result = ruleInteractor.addLetterToRule(
                letter, points,
                _latestRuleId.value, replace, ruleNamePrefix
            )
            _latestRuleId.value = result.ruleId
            _addLetterUiState.update {
                uiStateMapper.map(result, closeAfter)
            }
        }
    }

    companion object {
        fun factory(
            assistedFactory: EditLetterViewModelFactory,
            ruleId: Long,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(ruleId) as T
            }
        }
    }
}

@AssistedFactory
interface EditLetterViewModelFactory {
    fun create(ruleId: Long): EditLetterViewModel
}