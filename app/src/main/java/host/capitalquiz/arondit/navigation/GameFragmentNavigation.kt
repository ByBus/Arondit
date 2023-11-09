package host.capitalquiz.arondit.navigation

import androidx.navigation.NavController
import host.capitalquiz.game.ui.GameFragmentDirections
import host.capitalquiz.game.ui.GameNavigation
import javax.inject.Inject

class GameFragmentNavigation @Inject constructor(
    private val navController: NavController
): GameNavigation {
    override fun navigateToToAddPlayerDialog(playerColor: Int) {
        val addPlayerDialog =
            GameFragmentDirections.actionToAddPlayerDialog(playerColor)
        navController.navigate(addPlayerDialog)
    }

    override fun navigateToRemovePlayerDialog(playerId: Long, playerColor: Int) {
        val removePlayerDialog =
            GameFragmentDirections.actionToRemovePlayerDialog(playerId, playerColor)
        navController.navigate(removePlayerDialog)
    }

    override fun navigateToAddWordDialog(playerId: Long, playerColor: Int) {
        val addWordDialog =
            GameFragmentDirections.actionAddWordDialog(playerId, playerColor)
        navController.navigate(addWordDialog)
    }

    override fun navigateToEditWordDialog(wordId: Long, playerId: Long, playerColor: Int) {
        val editWordDialog =
            GameFragmentDirections.actionToEditWordDialog(
                wordId,
                playerColor,
                playerId
            )
        navController.navigate(editWordDialog)
    }
}