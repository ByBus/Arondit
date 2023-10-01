package host.capitalquiz.arondit.gameslist.ui

interface GamesListFragmentNavigation {
    fun navigateToRemoveGameDialog(gameId: Long)
    fun navigateToGame(gameId: Long)
    fun navigateToOnBoarding(gameId: Long)
}