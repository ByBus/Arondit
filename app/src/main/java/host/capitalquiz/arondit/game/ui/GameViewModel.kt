package host.capitalquiz.arondit.game.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.arondit.game.domain.Player
import host.capitalquiz.arondit.game.domain.PlayerInteractor
import host.capitalquiz.arondit.game.domain.PlayerMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val playerInteractor: PlayerInteractor,
    private val mapper: PlayerMapper<PlayerUi>,
    savedStateHandle: SavedStateHandle) : ViewModel() {
    private var gameId = 0L
    init {
        gameId = savedStateHandle["gameId"] ?: 0L
    }
    private val availableColors = mutableListOf<Int>()

    private var _players = playerInteractor.findAllPlayersOfGame(gameId)
    val players: LiveData<List<PlayerUi>> = _players.map { players ->
        players.map { it.map(mapper) }
    }

    fun addPlayersColors(colors: List<Int>) {
        availableColors.clear()
        availableColors.addAll(colors)
    }

    fun addPlayer(player: Player) {
        viewModelScope.launch {
            playerInteractor.createPlayer(player, gameId)
        }
    }

    fun deletePlayer(playerId: Long) {
        viewModelScope.launch {
            playerInteractor.deletePlayer(playerId)
        }
    }

    fun borrowColor(colorConsumer: (Int) -> Unit){
        if (availableColors.isNotEmpty()){
            colorConsumer(availableColors.removeLast())
        }
    }

    fun removeUsedColors(colors: List<Int>){
        availableColors.removeAll(colors)
    }

    fun returnColor(color: Int) {
        availableColors.add(color)
    }
}