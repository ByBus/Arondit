package host.capitalquiz.game.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.FieldInteractor
import host.capitalquiz.game.domain.GameRuleInteractor
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.mappers.FieldMapperWithParameter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

//@HiltViewModel
class GameViewModel @AssistedInject constructor(
    private val fieldInteractor: FieldInteractor,
    private val uiFieldMapper: FieldMapperWithParameter<GameRuleSimple, FieldUi>,
    private val gameRuleInteractor: GameRuleInteractor,
    @Assisted private val gameId: Long,
) : ViewModel() {
    private val availableColors = mutableListOf<Int>()

    private var _fields = fieldInteractor.findAllFieldsOfGame(gameId)
    val fields: LiveData<List<FieldUi>> = _fields.map { fields ->
        val rule = gameRuleInteractor.findRuleOfGame(gameId)
        fields.map { uiFieldMapper.map(it, rule) }
    }.asLiveData()

    fun addFieldsColors(colors: List<Int>) {
        availableColors.clear()
        availableColors.addAll(colors)
    }

    fun addPlayer(name: String, color: Int) {
        viewModelScope.launch {
            fieldInteractor.createField(Field(name = name, color = color), gameId)
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