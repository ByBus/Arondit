package host.capitalquiz.arondit.game.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.arondit.game.domain.Word
import host.capitalquiz.arondit.game.domain.WordInteractor
import host.capitalquiz.arondit.game.domain.WordMapper
import host.capitalquiz.arondit.game.ui.WordUi
import host.capitalquiz.arondit.game.ui.WordUiMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordDialogViewModel @Inject constructor(
    private val wordInteractor: WordInteractor,
    private val wordToUiMapper: WordMapper<WordUi>,
    private val wordUiToWordMapper: WordUiMapper<Word>
    ): ViewModel() {

    private val tempWord = MutableLiveData(WordUi())
    val word: LiveData<WordUi> = tempWord

    fun deleteWord(wordId: Long) {
        viewModelScope.launch {
            wordInteractor.deleteWord(wordId)
        }
    }

    fun loadWord(wordId: Long){
        viewModelScope.launch {
            tempWord.value = wordInteractor.word(wordId).map(wordToUiMapper)
        }
    }

    fun updateWord(word: String) {
        tempWord.value = tempWord.value?.copy(word = word)
    }

    fun updateScoreOfLetter(score: Int, letterIndex: Int) {
        tempWord.value?.let { word ->
            if (letterIndex < word.letterBonuses.size) {
                val wordScores = word.letterBonuses.toMutableList()
                wordScores[letterIndex] = score
                tempWord.value = word.copy(letterBonuses = wordScores)
            }
        }
    }

    fun updateWordMultiplier(value: Int){
        tempWord.value = tempWord.value?.copy(multiplier = value)
    }

    fun updateWord(playerId: Long) {
        tempWord.value?.let {
            viewModelScope.launch {
                wordInteractor.updateWord(playerId, it.map(wordUiToWordMapper))
            }
        }
    }

    fun saveWord(playerId: Long) {
        tempWord.value?.let {
            viewModelScope.launch {
                wordInteractor.addWord(playerId, it.map(wordUiToWordMapper))
            }
        }
    }
}