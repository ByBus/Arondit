package host.capitalquiz.game.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import host.capitalquiz.core.ui.liveData
import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.FieldInteractor
import host.capitalquiz.game.domain.GameRuleInteractor
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.mappers.FieldMapperWithParameter
import host.capitalquiz.game.ui.dialog.AddPlayerEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

//@HiltViewModel
class GameViewModel @AssistedInject constructor(
    private val fieldInteractor: FieldInteractor,
    private val uiFieldMapper: FieldMapperWithParameter<GameRuleSimple, FieldUi>,
    private val gameRuleInteractor: GameRuleInteractor,
    @Assisted private val gameId: Long,
) : ViewModel() {
    private val availableColors = mutableListOf<Int>()

    private val _fields = fieldInteractor.findAllFieldsOfGame(gameId)
    val fields = _fields.map { fields ->
        val rule = gameRuleInteractor.findRuleOfGame(gameId)
        fields.map { uiFieldMapper.map(it, rule) }
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

    fun deletePlayer(id: Long) {
        viewModelScope.launch {
            fieldInteractor.deleteField(id)
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

    fun loadAvailablePlayers() {
        viewModelScope.launch {
            _availablePlayers.value = fieldInteractor.findAllPlayersWhoIsNotPlayingYet(gameId)
        }
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