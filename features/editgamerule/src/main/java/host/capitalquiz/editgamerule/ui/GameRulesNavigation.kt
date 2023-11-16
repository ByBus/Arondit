package host.capitalquiz.editgamerule.ui

interface GameRulesNavigation {
    fun navigateToCreateRule()
    fun navigateToEditRule(ruleId: Long)
}