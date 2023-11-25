package host.capitalquiz.game.domain

import kotlinx.coroutines.flow.Flow

interface FieldRepository {

    fun allFieldsOfGame(gameId: Long): Flow<List<Field>>

    suspend fun createPlayer(field: Field, gameId: Long): Long

    suspend fun deleteField(filedId: Long)
}