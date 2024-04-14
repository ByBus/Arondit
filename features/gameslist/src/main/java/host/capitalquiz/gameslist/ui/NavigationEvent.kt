package host.capitalquiz.gameslist.ui

interface NavigationEvent {

    fun navigate(navigation: GamesListNavigation)

    object Idle : NavigationEvent {
        override fun navigate(navigation: GamesListNavigation) = Unit
    }

    class GameScreen(private val gameId: Long) : NavigationEvent {
        override fun navigate(navigation: GamesListNavigation) =
            navigation.navigateToGame(gameId)
    }

    class OnBoardingScreen(private val gameId: Long) : NavigationEvent {
        override fun navigate(navigation: GamesListNavigation) =
            navigation.navigateToOnBoarding(gameId)
    }

    class RemoveGameDialog(private val gameId: Long) : NavigationEvent {
        override fun navigate(navigation: GamesListNavigation) =
            navigation.navigateToRemoveGameDialog(gameId)
    }

    class EditGameRuleScreen(private val gameId: Long) : NavigationEvent {
        override fun navigate(navigation: GamesListNavigation) {
            navigation.navigateToEditGameRule(gameId)
        }
    }

    object StatisticsScreen : NavigationEvent {
        override fun navigate(navigation: GamesListNavigation) {
            navigation.navigateToStatistics()
        }
    }
}