package host.capitalquiz.game.domain

import kotlinx.coroutines.flow.Flow

interface FieldRepository {

    fun allFieldsOfGame(gameId: Long): Flow<List<Field>>

    suspend fun createField(field: Field, gameId: Long): Long

    suspend fun deleteField(filedId: Long)

    suspend fun allPlayers(): List<Player>

    suspend fun fieldsOfGame(gameId: Long): List<Field>

    suspend fun createPlayerWithName(name: String): Long

    suspend fun renamePlayer(name: String, playerId: Long)

    suspend fun fieldById(fieldId: Long): Field

    suspend fun findFieldsIdsWithPlayer(playerId: Long): List<Long>

    suspend fun deletePlayer(playerId: Long)
}