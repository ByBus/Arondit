package host.capitalquiz.arondit.navigation

import androidx.navigation.NavController
import host.capitalquiz.statistics.ui.StatisticsNavigation
import javax.inject.Inject

class StatisticsFragmentNavigation @Inject constructor(
    private val navController: NavController,
) : StatisticsNavigation {
    override fun navigateUp() {
        navController.navigateUp()
    }
}