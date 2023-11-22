package host.capitalquiz.editgamerule.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import host.capitalquiz.editgamerule.di.CreationMode
import host.capitalquiz.editgamerule.di.EditMode
import host.capitalquiz.editgamerule.domain.EditGameRuleInteractor
import host.capitalquiz.editgamerule.domain.LetterResult
import host.capitalquiz.editgamerule.domain.LetterAddResultMapper
import host.capitalquiz.editgamerule.domain.LetterAddResultMapperWithParameter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

typealias CreateResultToUiMapper<AddLetterUiState> = LetterAddResultMapperWithParameter<Boolean, AddLetterUiState>

class EditLetterViewModel @AssistedInject constructor(
    private val ruleInteractor: EditGameRuleInteractor,
    @CreationMode private val uiStateMapperCreate: CreateResultToUiMapper<AddLetterUiState>,
    @EditMode private val uiStateMapperEdit: LetterAddResultMapper<AddLetterUiState>,
    @Assisted private val ruleId: Long,
) : ViewModel() {
    private var mode = CREATION_MODE
    private var ruleNamePrefix: String = ""
    private var editingLetter: Char = ' '
    private val _latestRuleId = MutableStateFlow(ruleId)
    val latestRuleId = _latestRuleId.asStateFlow()

    private var _addLetterUiState = MutableStateFlow<AddLetterUiState>(AddLetterUiState.InitCreationMode)
    val addLetterUiState = _addLetterUiState.asStateFlow()

    fun init(ruleCopyNamePrefix: String) {
        ruleNamePrefix = ruleCopyNamePrefix
    }

    fun initLetter(letter: Char, point: Int, creationMode: Boolean) {
        mode = if (creationMode) CREATION_MODE else EDIT_MODE
        _addLetterUiState.update {
            if (mode == CREATION_MODE)
                AddLetterUiState.CreateNextLetter(letter, point)
            else
                AddLetterUiState.InitEditMode(letter, point).apply {
                    editingLetter = letter
                }
        }
    }

    fun saveLetter(letter: Char, points: Int, closeAfter: Boolean, replace: Boolean = false) {
        viewModelScope.launch {
            if (mode == CREATION_MODE)
                createNewLetter(letter, points, replace, closeAfter)
            else
                editLetter(letter, points)
        }
    }

    fun deleteCurrentLetter() {
        viewModelScope.launch {
            val result = ruleInteractor.deleteLetterFromRule(editingLetter, latestRuleId.value, ruleNamePrefix)
            updateUiStateFromEditResult(result)
        }
    }

    private suspend fun createNewLetter(
        letter: Char,
        points: Int,
        replace: Boolean,
        closeAfter: Boolean,
    ) {
        val result = ruleInteractor.addLetterToRule(
            letter, points,
            _latestRuleId.value, replace, ruleNamePrefix
        )
        _latestRuleId.value = result.ruleId
        _addLetterUiState.update {
            uiStateMapperCreate.map(result, closeAfter)
        }
    }

    private suspend fun editLetter(letter: Char, points: Int) {
        val result = ruleInteractor.editLetterOfRule(
            editingLetter, letter, points, _latestRuleId.value, ruleNamePrefix
        )
        updateUiStateFromEditResult(result)
    }

    private fun updateUiStateFromEditResult(result: LetterResult) {
        _latestRuleId.value = result.ruleId
        _addLetterUiState.update {
            result.map(uiStateMapperEdit)
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

        private const val CREATION_MODE = 1
        private const val EDIT_MODE = 0
    }
}

@AssistedFactory
interface EditLetterViewModelFactory {
    fun create(ruleId: Long): EditLetterViewModel
}