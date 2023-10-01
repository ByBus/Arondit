package host.capitalquiz.arondit.gameslist.ui

interface NavigationState {

    fun navigate(navigation: GamesListFragmentNavigation)

    object Idle : NavigationState {
        override fun navigate(navigation: GamesListFragmentNavigation) = Unit
    }

    class GameScreen(private val gameId: Long) : NavigationState {
        override fun navigate(navigation: GamesListFragmentNavigation) =
            navigation.navigateToGame(gameId)
    }

    class OnBoardingScreen(private val gameId: Long) : NavigationState {
        override fun navigate(navigation: GamesListFragmentNavigation) =
            navigation.navigateToOnBoarding(gameId)
    }

    class RemoveGameDialog(private val gameId: Long) : NavigationState {
        override fun navigate(navigation: GamesListFragmentNavigation) =
            navigation.navigateToRemoveGameDialog(gameId)
    }

}