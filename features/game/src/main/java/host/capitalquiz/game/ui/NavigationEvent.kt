package host.capitalquiz.game.ui

interface NavigationEvent {
    fun navigate(navigator: GameNavigation)

    class AddPlayerDialog(private val color: Int) : NavigationEvent {
        override fun navigate(navigator: GameNavigation) =
            navigator.navigateToToAddPlayerDialog(color)

    }

    class RemovePlayerDialog(private val fieldId: Long, private val color: Int) : NavigationEvent {
        override fun navigate(navigator: GameNavigation) =
            navigator.navigateToRemovePlayerDialog(fieldId, color)
    }

    class AddWordDialog(private val fieldId: Long, private val color: Int) : NavigationEvent {
        override fun navigate(navigator: GameNavigation) =
            navigator.navigateToAddWordDialog(fieldId, color)
    }

    class EditWordDialog(
        private val wordId: Long,
        private val fieldId: Long,
        private val color: Int,
    ) : NavigationEvent {
        override fun navigate(navigator: GameNavigation) =
            navigator.navigateToEditWordDialog(wordId, fieldId, color)
    }

    class RenamePlayerDialog(
        private val oldName: String,
        private val playerId: Long,
        private val color: Int,
    ) : NavigationEvent {
        override fun navigate(navigator: GameNavigation) =
            navigator.navigateToRenamePlayerDialog(oldName, playerId, color)
    }
}