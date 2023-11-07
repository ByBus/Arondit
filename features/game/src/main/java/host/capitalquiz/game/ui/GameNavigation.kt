package host.capitalquiz.game.ui

interface GameNavigation {
    fun navigateToToAddPlayerDialog(color: Int)
    fun navigateToRemovePlayerDialog(playerId: Long, color: Int)
    fun navigateToAddWordDialog(playerId: Long, color: Int)
    fun navigateToEditWordDialog(wordId: Long, playerId: Long, color: Int)
}