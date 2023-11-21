package host.capitalquiz.editgamerule.ui.editscreen

interface EditGameRuleNavigation {
    fun navigateToRenameRuleDialog(oldName: String)
    fun navigateToAddLetterDialog(ruleId: Long, letter: Char?, points: Int)
    fun navigateUp()
}