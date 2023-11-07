package host.capitalquiz.arondit.gameslist.ui

interface NavigationState {

    fun navigate(navigation: GamesListNavigation)

    object Idle : NavigationState {
        override fun navigate(navigation: GamesListNavigation) = Unit
    }

    class GameScreen(private val gameId: Long) : NavigationState {
        override fun navigate(navigation: GamesListNavigation) =
            navigation.navigateToGame(gameId)
    }

    class OnBoardingScreen(private val gameId: Long) : NavigationState {
        override fun navigate(navigation: GamesListNavigation) =
            navigation.navigateToOnBoarding(gameId)
    }

    class RemoveGameDialog(private val gameId: Long) : NavigationState {
        override fun navigate(navigation: GamesListNavigation) =
            navigation.navigateToRemoveGameDialog(gameId)
    }

}