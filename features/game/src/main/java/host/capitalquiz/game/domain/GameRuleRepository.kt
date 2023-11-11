package host.capitalquiz.game.domain

interface GameRuleRepository {
    suspend fun gameRuleOfGame(gameId: Long): GameRuleSimple

    fun readLastRule(): GameRuleSimple
}