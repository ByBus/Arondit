package host.capitalquiz.arondit.game.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.arondit.game.domain.WordInteractor
import host.capitalquiz.arondit.game.domain.WordMapper
import host.capitalquiz.arondit.game.ui.WordUi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordDialogViewModel @Inject constructor(
    private val wordInteractor: WordInteractor,
    private val wordToUiMapper: WordMapper<WordUi>,
): ViewModel() {

    private val tempWord = wordInteractor.readCache()
    val word: LiveData<WordUi> = tempWord.map { it.map(wordToUiMapper) }

    fun deleteWord(wordId: Long) {
        viewModelScope.launch { wordInteractor.deleteWord(wordId) }
    }

    fun initWord(playerId: Long){
        viewModelScope.launch { wordInteractor.initCacheWithPlayer(playerId) }
    }

    fun loadWord(wordId: Long){
        viewModelScope.launch { wordInteractor.loadWordToCache(wordId) }
    }

    fun updateWord(word: String) {
        viewModelScope.launch { wordInteractor.updateWord(word) }
    }

    fun updateWordMultiplier(value: Int){
        viewModelScope.launch { wordInteractor.updateMultiplier(value) }
    }

    fun saveWord() {
        viewModelScope.launch { wordInteractor.saveCachedWord() }
    }

    fun changeLetterScore(index: Int) {
        viewModelScope.launch { wordInteractor.updateScore(index) }
    }
}