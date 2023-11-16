package host.capitalquiz.editgamerule.ui.ruleslist

data class GameRuleUi(
    val id: Long,
    val name: String,
    val points: Map<Char, Int>,
    val readOnly: Boolean,
    val gamesIds: List<Long>,
    val selected: Boolean
)