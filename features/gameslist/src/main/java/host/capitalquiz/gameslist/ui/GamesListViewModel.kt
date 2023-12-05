package host.capitalquiz.gameslist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.gameslist.domain.GamesListInteractor
import host.capitalquiz.gameslist.domain.mappers.GameMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NO_GAME_ID = -1L

@HiltViewModel
class GamesListViewModel @Inject constructor(
    private val gamesListInteractor: GamesListInteractor,
    private val gamesMapper: GameMapper<GameUi>,
) : ViewModel() {
    val games = gamesListInteractor.allGames().map { games ->
        games.map { it.map(gamesMapper) }
    }

    val showStatisticsButton = games.map { games ->
        games.any { game -> game.playersColorWithInfo.isNotEmpty() }
    }

    private val shouldShowOnBoardingScreen = gamesListInteractor.showOnBoardingScreen
    private val gameId = MutableStateFlow(NO_GAME_ID)
    private val _navigationState = Channel<NavigationState>()
    val navigationState = _navigationState.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(
                shouldShowOnBoardingScreen, gameId
            ) { showOnBoarding, id ->
                when  {
                    id == NO_GAME_ID -> NavigationState.Idle
                    showOnBoarding -> NavigationState.OnBoardingScreen(id)
                    else -> NavigationState.GameScreen(id)
                }
            }.collect{
                _navigationState.trySend(it)
            }
        }
    }

    fun createGame() {
        viewModelScope.launch {
            gameId.value = gamesListInteractor.createGame()
        }
    }

    fun showRemoveGameDialog(gameId: Long) {
        viewModelScope.launch {
            _navigationState.trySend(NavigationState.RemoveGameDialog(gameId))
        }
    }

    fun removeGame(gameId: Long){
        viewModelScope.launch {
            gamesListInteractor.deleteGame(gameId)
        }
    }

    fun showGame(gameId: Long) {
        this.gameId.value = NO_GAME_ID
        this.gameId.value = gameId
    }

    fun showEditGameRuleScreen(gameId: Long) {
        viewModelScope.launch {
            _navigationState.trySend(NavigationState.EditGameRuleScreen(gameId))
        }
    }

    fun showStatisticsScreen() {
        viewModelScope.launch {
            _navigationState.trySend(NavigationState.StatisticsScreen)
        }
    }
}