package host.capitalquiz.game.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.core.ui.liveData
import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.FieldInteractor
import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.mappers.GameMapper
import host.capitalquiz.game.ui.dialog.AddPlayerEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GameViewModelFactory::class)
class GameViewModel @AssistedInject constructor(
    private val fieldInteractor: FieldInteractor,
    private val gameToFieldsUiMapper: GameMapper<List<FieldUi>>,
    @Assisted private val gameId: Long,
) : ViewModel() {
    private val availableColors = mutableListOf<Int>()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _game = fieldInteractor.findGame(gameId)
    val fields = _game.map { game ->
        game.map(gameToFieldsUiMapper)
    }

    private val _availablePlayers = MutableLiveData<List<Player>>(emptyList())
    val availablePlayers = _availablePlayers.liveData()

    private val _playerAddedEvent = Channel<AddPlayerEvent>()
    val playerAddedEvent = _playerAddedEvent.receiveAsFlow()

    fun addFieldsColors(colors: List<Int>) {
        availableColors.clear()
        availableColors.addAll(colors)
    }

    fun addPlayer(name: String, color: Int, playerId: Long? = null) {
        var field = Field(name = name, color = color)
        if (playerId != null) field = field.copy(playerId = playerId)
        viewModelScope.launch {
            val result = fieldInteractor.createField(field, gameId)
            _playerAddedEvent.trySend(AddPlayerEvent(result))
        }
    }

    fun renamePlayer(newName: String, playerId: Long) {
        viewModelScope.launch {
            val result = fieldInteractor.renamePlayer(newName, playerId)
            _playerAddedEvent.trySend(AddPlayerEvent(result))
        }
    }

    fun deletePlayer(id: Long) {
        viewModelScope.launch {
            fieldInteractor.deleteField(id)
        }
    }

    fun removeUsedColors(colors: List<Int>) {
        availableColors.removeAll(colors)
    }

    fun returnColor(color: Int) {
        availableColors.add(color)
    }

    fun loadAvailablePlayers() {
        viewModelScope.launch {
            _availablePlayers.value = fieldInteractor.findAllPlayersWhoIsNotPlayingYet(gameId)
        }
    }

    fun goToAddPlayerDialog() {
        borrowColor { color ->
            _navigationEvent.trySend(NavigationEvent.AddPlayerDialog(color))
        }
    }

    fun goToRemovePlayerDialog(fieldId: Long, color: Int) {
        _navigationEvent.trySend(NavigationEvent.RemovePlayerDialog(fieldId, color))
    }

    fun goToAddWordDialog(fieldId: Long, color: Int) {
        _navigationEvent.trySend(NavigationEvent.AddWordDialog(fieldId, color))
    }

    fun goToEditWordDialog(wordId: Long, fieldId: Long, color: Int) {
        _navigationEvent.trySend(NavigationEvent.EditWordDialog(wordId, fieldId, color))
    }

    fun goToRenamePlayerDialog(oldName: String, playerId: Long, color: Int) {
        _navigationEvent.trySend(NavigationEvent.RenamePlayerDialog(oldName, playerId, color))
    }

    private fun borrowColor(colorConsumer: (Int) -> Unit) {
        if (availableColors.isNotEmpty()) {
            colorConsumer(availableColors.removeLast())
        }
    }
}

@AssistedFactory
interface GameViewModelFactory {
    fun create(gameId: Long): GameViewModel
}