package host.capitalquiz.game.domain

import kotlinx.coroutines.flow.Flow

interface FieldRepository {

    fun allFieldsOfGame(gameId: Long): Flow<List<Field>>

    suspend fun createField(field: Field, gameId: Long): Long

    suspend fun deleteField(filedId: Long)

    suspend fun allPlayers(): List<Player>

    suspend fun fieldsOfGame(gameId: Long): List<Field>

    suspend fun createPlayerWithName(name: String): Long
}