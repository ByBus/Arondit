package host.capitalquiz.game.ui

interface GameNavigation {
    fun navigateToToAddPlayerDialog(color: Int)
    fun navigateToRemovePlayerDialog(fieldId: Long, color: Int)
    fun navigateToAddWordDialog(fieldId: Long, color: Int)
    fun navigateToEditWordDialog(wordId: Long, fieldId: Long, color: Int)
}