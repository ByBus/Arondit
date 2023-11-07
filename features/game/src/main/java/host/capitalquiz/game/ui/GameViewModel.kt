package host.capitalquiz.game.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.PlayerInteractor
import host.capitalquiz.game.domain.PlayerMapper
import kotlinx.coroutines.launch

//@HiltViewModel
class GameViewModel @AssistedInject constructor(
    private val playerInteractor: PlayerInteractor,
    private val mapper: PlayerMapper<PlayerUi>,
    @Assisted private val gameId: Long,
) : ViewModel() {
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

    fun borrowColor(colorConsumer: (Int) -> Unit) {
        if (availableColors.isNotEmpty()) {
            colorConsumer(availableColors.removeLast())
        }
    }

    fun removeUsedColors(colors: List<Int>) {
        availableColors.removeAll(colors)
    }

    fun returnColor(color: Int) {
        availableColors.add(color)
    }

    companion object {
        fun provideFactory(
            assistedFactory: GameViewModelFactory,
            gameId: Long,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(gameId) as T
            }
        }
    }
}

@AssistedFactory
interface GameViewModelFactory {
    fun create(gameId: Long): GameViewModel
}