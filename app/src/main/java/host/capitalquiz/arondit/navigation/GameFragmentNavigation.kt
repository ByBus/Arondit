package host.capitalquiz.arondit.navigation

import androidx.navigation.NavController
import host.capitalquiz.game.ui.GameFragmentDirections
import host.capitalquiz.game.ui.GameNavigation
import javax.inject.Inject

class GameFragmentNavigation @Inject constructor(
    private val navController: NavController
): GameNavigation {
    override fun navigateToToAddPlayerDialog(color: Int) {
        val addPlayerDialog =
            GameFragmentDirections.actionToAddPlayerDialog(color)
        navController.navigate(addPlayerDialog)
    }

    override fun navigateToRemovePlayerDialog(fieldId: Long, color: Int) {
        val removePlayerDialog =
            GameFragmentDirections.actionToRemovePlayerDialog(fieldId, color)
        navController.navigate(removePlayerDialog)
    }

    override fun navigateToAddWordDialog(fieldId: Long, color: Int) {
        val addWordDialog =
            GameFragmentDirections.actionAddWordDialog(fieldId, color)
        navController.navigate(addWordDialog)
    }

    override fun navigateToEditWordDialog(wordId: Long, fieldId: Long, color: Int) {
        val editWordDialog =
            GameFragmentDirections.actionToEditWordDialog(
                wordId,
                color,
                fieldId
            )
        navController.navigate(editWordDialog)
    }
}