package host.capitalquiz.editgamerule.domain

data class GameRule(
    val id: Long,
    val name: String,
    val points: Map<Char, Int>,
    val readOnly: Boolean,
    val gamesIds: List<Long>,
) {
    fun usedByGame(gameId: Long): Boolean = gamesIds.contains(gameId)

    fun <R> map(mapper: GameRuleMapper<R>): R {
        return mapper(id, name, points, readOnly, gamesIds)
    }

    fun hasLetter(letter: Char): Boolean = points.containsKey(letter.uppercaseChar())

    fun points(letter: Char): Int = points.getOrDefault(letter.uppercaseChar(), 0)

}