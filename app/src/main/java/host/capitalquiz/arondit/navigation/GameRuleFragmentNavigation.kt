package host.capitalquiz.arondit.navigation

import androidx.navigation.NavController
import host.capitalquiz.editgamerule.ui.ruleslist.GameRulesNavigation
import host.capitalquiz.editgamerule.ui.ruleslist.GameRulesFragmentDirections
import javax.inject.Inject

class GameRuleFragmentNavigation @Inject constructor(
    private val navController: NavController,
) : GameRulesNavigation {
    override fun navigateToCreateRule() {
        val actionCreateRule = GameRulesFragmentDirections.actionToEditGameRuleFragment(-1L)
        navController.navigate(actionCreateRule)
    }

    override fun navigateToEditRule(ruleId: Long) {
        val actionEditRule = GameRulesFragmentDirections.actionToEditGameRuleFragment(ruleId)
        navController.navigate(actionEditRule)
    }
}