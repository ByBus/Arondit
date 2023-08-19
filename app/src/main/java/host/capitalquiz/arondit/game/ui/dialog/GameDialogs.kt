package host.capitalquiz.arondit.game.ui.dialog

interface GameDialogs {
    fun openRemovePlayerDialog(playerId: Long, playerColor: Int)
    fun openAddWordDialog(playerId: Long, playerColor: Int)
    fun openEditWordDialog(wordId: Long, playerId: Long, dialogColor: Int)
    fun openAddPlayerDialog()
}