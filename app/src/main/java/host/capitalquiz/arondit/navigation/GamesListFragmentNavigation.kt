package host.capitalquiz.arondit.navigation

import androidx.navigation.NavController
import host.capitalquiz.arondit.BaseNavigationDirections
import host.capitalquiz.gameslist.ui.GamesListFragmentDirections
import host.capitalquiz.gameslist.ui.GamesListNavigation
import javax.inject.Inject

class GamesListFragmentNavigation @Inject constructor(
    private val navController: NavController
) : GamesListNavigation {
    override fun navigateToRemoveGameDialog(gameId: Long) {
        val removeDialogAction = GamesListFragmentDirections.actionToRemoveGameDialog(gameId)
        navController.navigate(removeDialogAction)
    }

    override fun navigateToGame(gameId: Long) {
        val gameAction = BaseNavigationDirections.actionToGame(gameId)
        navController.navigate(gameAction)
    }

    override fun navigateToOnBoarding(gameId: Long) {
        val onboardingAction = BaseNavigationDirections.actionToOnboarding(gameId)
        navController.navigate(onboardingAction)
    }

    override fun navigateToEditGameRule(gameId: Long) {
        val editGameRuleAction = BaseNavigationDirections.actionToEditGameRule(gameId)
        navController.navigate(editGameRuleAction)
    }
}