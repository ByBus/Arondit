package host.capitalquiz.game.ui


data class FieldUi(
    val id: Long,
    val color: Int,
    val playerName: String,
    val words: List<WordUi>,
    val score: Int,
    val playerId: Long,
)
