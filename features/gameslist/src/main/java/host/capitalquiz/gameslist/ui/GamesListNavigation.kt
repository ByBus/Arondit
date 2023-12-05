package host.capitalquiz.gameslist.ui

interface GamesListNavigation {
    fun navigateToRemoveGameDialog(gameId: Long)
    fun navigateToGame(gameId: Long)
    fun navigateToOnBoarding(gameId: Long)
    fun navigateToEditGameRule(gameId: Long)
    fun navigateToStatistics()
}