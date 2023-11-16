package host.capitalquiz.editgamerule.ui.editscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import host.capitalquiz.editgamerule.domain.EditGameRuleInteractor
import host.capitalquiz.editgamerule.domain.GameRuleMapper
import kotlinx.coroutines.flow.map

class EditGameRuleViewModel @AssistedInject constructor(
    private val ruleInteractor: EditGameRuleInteractor,
    private val ruleToUiMapper: GameRuleMapper<EditableGameRuleUi>,
    @Assisted private val ruleId: Long,
) : ViewModel() {

    val gameRule = ruleInteractor.getGameRule(ruleId).map {
        it.map(ruleToUiMapper)
    }.asLiveData()

    companion object {
        fun factory(
            assistedFactory: EditGameRuleViewModelFactory,
            ruleId: Long,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(ruleId) as T
            }
        }
    }
}


@AssistedFactory
interface EditGameRuleViewModelFactory {
    fun create(ruleId: Long): EditGameRuleViewModel
}
