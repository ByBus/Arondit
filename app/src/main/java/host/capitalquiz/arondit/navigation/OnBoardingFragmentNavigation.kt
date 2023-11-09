package host.capitalquiz.arondit.navigation

import androidx.navigation.NavController
import host.capitalquiz.arondit.BaseNavigationDirections
import host.capitalquiz.onboarding.ui.OnBoardingNavigation
import javax.inject.Inject

class OnBoardingFragmentNavigation @Inject constructor(
    private val navController: NavController
) : OnBoardingNavigation {
    override fun navigateToGameFragment(gameId: Long) {
        navController.popBackStack()
        val gameFragment = BaseNavigationDirections.actionToGame(gameId)
        navController.navigate(gameFragment)
    }

    override fun navigateBack() {
        navController.popBackStack()
    }

}