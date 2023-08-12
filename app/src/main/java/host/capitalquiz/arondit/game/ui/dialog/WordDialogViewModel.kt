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
        tempWord.value?.let{
            val bonuses = MutableList(word.length){ 1 }
            for ((i, value) in it.letterBonuses.withIndex()){
                if (i > word.lastIndex) break
                bonuses[i] = value
            }
            tempWord.value = tempWord.value?.copy(word = word, letterBonuses = bonuses)
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

    fun changeLetterScore(index: Int) {
        tempWord.value?.let {
            val bonuses = it.letterBonuses.toMutableList()
            bonuses[index] = if (bonuses[index] == 3) 1 else bonuses[index] + 1
            tempWord.value = tempWord.value?.copy(letterBonuses = bonuses)
        }
    }
}