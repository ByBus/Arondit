package host.capitalquiz.game.domain

interface PlayerRepository {
    suspend fun allPlayers(): List<Player>
    suspend fun createPlayerWithName(name: String): Long
    suspend fun renamePlayer(name: String, playerId: Long)
    suspend fun deletePlayer(playerId: Long)
}