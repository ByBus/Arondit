package host.capitalquiz.editgamerule.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import host.capitalquiz.editgamerule.domain.GameRuleInteractor
import kotlinx.coroutines.flow.map

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

    companion object {
        fun provideFactory(
            assistedFactory: GameRuleViewModelFactory,
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
interface GameRuleViewModelFactory {
    fun create(gameId: Long): GameRulesViewModel
}