package host.capitalquiz.arondit.navigation

import androidx.navigation.NavController
import host.capitalquiz.editgamerule.ui.editscreen.EditGameRuleFragmentDirections
import host.capitalquiz.editgamerule.ui.editscreen.EditGameRuleNavigation
import javax.inject.Inject

class EditGameRuleFragmentNavigation @Inject constructor(
    private val navController: NavController
) : EditGameRuleNavigation {
    override fun navigateToRenameRuleDialog(oldName: String) {
        val renameDialogAction =
            EditGameRuleFragmentDirections.actionToRenameRuleFragment(oldName)
        navController.navigate(renameDialogAction)
    }

    override fun navigateToAddLetterDialog(ruleId: Long, letter: Char?, points: Int) {
        val addLetterDialogAction =
            EditGameRuleFragmentDirections.actionToAddLetterDialogFragment(ruleId, letter?.toString(), points)
        navController.navigate(addLetterDialogAction)
    }

    override fun navigateUp() {
        navController.navigateUp()
    }
}