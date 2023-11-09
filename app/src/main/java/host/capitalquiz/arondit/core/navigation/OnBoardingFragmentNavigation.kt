package host.capitalquiz.arondit.core.navigation

import androidx.navigation.NavController
import host.capitalquiz.arondit.gameslist.ui.GamesListFragmentDirections
import host.capitalquiz.onboarding.ui.OnBoardingNavigation
import javax.inject.Inject

class OnBoardingFragmentNavigation @Inject constructor(
    private val navController: NavController
) : OnBoardingNavigation {
    override fun navigateToGameFragment(gameId: Long) {
        navController.popBackStack()
        val gameFragment = GamesListFragmentDirections.actionToGameFragment(gameId)
        navController.navigate(gameFragment)
    }

    override fun navigateBack() {
        navController.popBackStack()
    }

}