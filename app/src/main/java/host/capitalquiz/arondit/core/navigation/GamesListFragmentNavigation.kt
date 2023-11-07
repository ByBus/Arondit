package host.capitalquiz.arondit.core.navigation

import androidx.navigation.NavController
import host.capitalquiz.arondit.gameslist.ui.GamesListFragmentDirections
import host.capitalquiz.arondit.gameslist.ui.GamesListNavigation
import javax.inject.Inject

class GamesListFragmentNavigation @Inject constructor(
    private val navController: NavController
) : GamesListNavigation {
    override fun navigateToRemoveGameDialog(gameId: Long) {
        navController.navigate(GamesListFragmentDirections.actionToRemoveGameDialog(gameId))
    }

    override fun navigateToGame(gameId: Long) {
        navController.navigate(GamesListFragmentDirections.actionToGameFragment(gameId))
    }

    override fun navigateToOnBoarding(gameId: Long) {
        navController.navigate(GamesListFragmentDirections.actionToOnBoardingFragment(gameId))
    }
}