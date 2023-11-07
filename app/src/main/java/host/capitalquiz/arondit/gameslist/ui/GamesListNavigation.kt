package host.capitalquiz.arondit.gameslist.ui

interface GamesListNavigation {
    fun navigateToRemoveGameDialog(gameId: Long)
    fun navigateToGame(gameId: Long)
    fun navigateToOnBoarding(gameId: Long)
}