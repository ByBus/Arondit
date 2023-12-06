package host.capitalquiz.editgamerule.ui.ruleslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import host.capitalquiz.editgamerule.domain.GameRuleInteractor
import host.capitalquiz.editgamerule.ui.ruleslist.mappers.GameRuleToGameUiMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GameRuleViewModelFactory::class)
class GameRulesViewModel @AssistedInject constructor(
    private val gameRuleInteractor: GameRuleInteractor,
    private val gameUiMapper: GameRuleToGameUiMapper,
    @Assisted private var gameId: Long
) : ViewModel() {

    private val _gameRules = gameRuleInteractor.getAllRules()
    val gameRules = _gameRules.map { rules ->
        rules.map {
            gameUiMapper.map(it, gameId)
        }
    }.asLiveData()

    private val _deleteRuleErrorEvent = Channel<CantDeleteRuleEvent>()
    val deleteRuleErrorEvent = _deleteRuleErrorEvent.receiveAsFlow()

    fun selectRuleForGame(ruleId: Long) {
        viewModelScope.launch {
            gameRuleInteractor.updateRuleForGame(gameId, ruleId)
        }
    }

    fun deleteGameRule(ruleId: Long) {
        viewModelScope.launch {
            val isDeleted = gameRuleInteractor.deleteRule(ruleId, gameId)
            if (isDeleted.not()) _deleteRuleErrorEvent.trySend(CantDeleteRuleEvent())
        }
    }
}


@AssistedFactory
interface GameRuleViewModelFactory {
    fun create(gameId: Long): GameRulesViewModel
}