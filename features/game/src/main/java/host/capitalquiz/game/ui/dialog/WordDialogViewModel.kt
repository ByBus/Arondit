package host.capitalquiz.game.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.game.domain.GameRuleInteractor
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.WordInteractor
import host.capitalquiz.game.domain.mappers.WordDefinitionMapper
import host.capitalquiz.game.domain.mappers.WordMapperWithParameter
import host.capitalquiz.game.ui.WordUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TEXT_INPUT_DEBOUNCE_MILLIS = 500L

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class WordDialogViewModel @Inject constructor(
    private val wordInteractor: WordInteractor,
    private val wordToUiMapper: WordMapperWithParameter<GameRuleSimple, WordUi>,
    private val ruleInteractor: GameRuleInteractor,
    private val wordDefinitionToUiMapper: WordDefinitionMapper<WordDefinitionUi>,
) : ViewModel() {

    private val tempWord = wordInteractor.loadWord()
    val word: LiveData<WordUi>
        get() = tempWord.map {
            val rule = ruleInteractor.getLastGameRule()
            wordToUiMapper.map(it, rule)
        }

    private val queryFlow = MutableStateFlow("")
    private val _definition = MutableStateFlow<WordDefinitionUi>(WordDefinitionUi.NoDefinition)
    val definition = _definition.asStateFlow()

    private val duplicateWordEvent = MutableSharedFlow<Boolean>()
    val wordSavingResult = duplicateWordEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            queryFlow
                .debounce(TEXT_INPUT_DEBOUNCE_MILLIS)
                .onEach { word ->
                    wordInteractor.updateWord(word)
                }
                .filterNot { it.isBlank() }
                .distinctUntilChanged()
                .mapLatest { word ->
                    wordInteractor.findDefinition(word)
                }
                .collect { result ->
                    _definition.value = result.map(wordDefinitionToUiMapper)
                }
        }
    }

    fun deleteWord(wordId: Long) {
        viewModelScope.launch { wordInteractor.deleteWord(wordId) }
    }

    fun initWord(playerId: Long) {
        viewModelScope.launch { wordInteractor.initCacheWithField(playerId) }
    }

    fun loadWord(wordId: Long) {
        viewModelScope.launch { wordInteractor.loadWordToCache(wordId) }
    }

    fun updateWord(word: String) {
        viewModelScope.launch {
            queryFlow.value = word
        }
    }

    fun updateWordMultiplier(value: Int) {
        viewModelScope.launch { wordInteractor.updateMultiplier(value) }
    }

    fun saveWord() {
        viewModelScope.launch {
            val isSaved = wordInteractor.saveWord()
            duplicateWordEvent.emit(isSaved)
        }
    }

    fun changeLetterScore(index: Int) {
        viewModelScope.launch { wordInteractor.cycleLetterScore(index) }
    }

    fun switchLetterAsterisk(index: Int){
        viewModelScope.launch { wordInteractor.switchLetterAsterisk(index) }
    }

    fun showExtraScore(value: Boolean) {
        viewModelScope.launch { wordInteractor.updateExtraPoints(value) }
    }
}