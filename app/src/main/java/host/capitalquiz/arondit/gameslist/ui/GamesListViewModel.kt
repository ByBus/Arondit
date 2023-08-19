package host.capitalquiz.arondit.gameslist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.arondit.gameslist.domain.GameMapper
import host.capitalquiz.arondit.gameslist.domain.GamesListInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesListViewModel @Inject constructor(
    private val gamesListInteractor: GamesListInteractor,
    private val gamesMapper: GameMapper<GameUi>,
) : ViewModel() {
    private val _games = gamesListInteractor.allGames()
    val games: LiveData<List<GameUi>> = _games.map { games ->
        games.map { it.map(gamesMapper) }
    }

    private val _newGameId = MutableLiveData(-1L)
    val newGameId: LiveData<Long> = _newGameId

    private val _navigateToGameScreen = MutableLiveData(false)
    val navigateToGameScreen: LiveData<Boolean> = _navigateToGameScreen
    fun createGame() {
        viewModelScope.launch {
            _newGameId.value = gamesListInteractor.createGame()
            _navigateToGameScreen.value = true
        }
    }

    fun removeGame(gameId: Long){
        viewModelScope.launch {
            gamesListInteractor.deleteGame(gameId)
        }
    }

    fun resetNavigation() {
        _navigateToGameScreen.value = false
    }
}