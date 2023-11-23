package host.capitalquiz.game.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.game.domain.GameRuleInteractor
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.WordInteractor
import host.capitalquiz.game.domain.mappers.WordDefinitionMapper
import host.capitalquiz.game.domain.mappers.WordMapperWithParameter
import host.capitalquiz.game.ui.WordUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TEXT_INPUT_DEBOUNCE_MILLIS = 500L

@HiltViewModel
class WordDialogViewModel @Inject constructor(
    private val wordInteractor: WordInteractor,
    private val wordToUiMapper: WordMapperWithParameter<GameRuleSimple, WordUi>,
    private val ruleInteractor: GameRuleInteractor,
    private val wordDefinitionToUiMapper: WordDefinitionMapper<WordDefinitionUi>,
) : ViewModel() {

    private val tempWord = wordInteractor.loadWord()
    val word: LiveData<WordUi> get() = tempWord.map {
    val rule = ruleInteractor.getLastGameRule()
        wordToUiMapper.map(it, rule)
    }

    private val queryFlow = MutableStateFlow("")
    private val _definition = MutableStateFlow<WordDefinitionUi>(WordDefinitionUi.NoDefinition)
    val definition: LiveData<WordDefinitionUi>
        get() = _definition.asLiveData(viewModelScope.coroutineContext)

    private val duplicateWordChannel = Channel<Boolean>()
    val wordSavingResult = duplicateWordChannel.receiveAsFlow()
    init {
        viewModelScope.launch {
            queryFlow
                .sample(TEXT_INPUT_DEBOUNCE_MILLIS)
                .distinctUntilChanged()
                .mapLatest {
                    wordInteractor.findDefinition(it)
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
        viewModelScope.launch { wordInteractor.initCacheWithPlayer(playerId) }
    }

    fun loadWord(wordId: Long) {
        viewModelScope.launch { wordInteractor.loadWordToCache(wordId) }
    }

    fun updateWord(word: String) {
        viewModelScope.launch {
            wordInteractor.updateWord(word)
            queryFlow.value = word
        }
    }

    fun updateWordMultiplier(value: Int) {
        viewModelScope.launch { wordInteractor.updateMultiplier(value) }
    }

    fun saveWord() {
        viewModelScope.launch {
            val isSaved = wordInteractor.saveWord()
            duplicateWordChannel.send(isSaved)
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