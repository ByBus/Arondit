package host.capitalquiz.editgamerule.ui.ruleslist

interface GameRulesNavigation {
    fun navigateToCreateRule()
    fun navigateToEditRule(ruleId: Long)
}