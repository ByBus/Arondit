package host.capitalquiz.editgamerule.ui.editscreen

interface NavigationEvent {
    fun consume(navigator: EditGameRuleNavigation)

    class AddLetterDialog(val ruleId: Long, val letter: Char?, val points: Int) : NavigationEvent {
        override fun consume(navigator: EditGameRuleNavigation) =
            navigator.navigateToAddLetterDialog(ruleId, letter, points)
    }

    object Up : NavigationEvent {
        override fun consume(navigator: EditGameRuleNavigation) = navigator.navigateUp()
    }

    class RenameRuleDialog(private val oldName: String) : NavigationEvent {
        override fun consume(navigator: EditGameRuleNavigation) =
            navigator.navigateToRenameRuleDialog(oldName)
    }

}
