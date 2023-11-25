package host.capitalquiz.game.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FieldInteractor {

    fun findAllFieldsOfGame(gameId: Long): Flow<List<Field>>

    suspend fun createField(field: Field, gameId: Long): Long

    suspend fun deleteField(playerId: Long)

    class Base @Inject constructor(
        private val fieldRepository: FieldRepository,
    ) : FieldInteractor {
        override fun findAllFieldsOfGame(gameId: Long): Flow<List<Field>> {
            return fieldRepository.allFieldsOfGame(gameId)
        }

        override suspend fun createField(field: Field, gameId: Long): Long {
            return fieldRepository.createPlayer(field, gameId)
        }

        override suspend fun deleteField(playerId: Long) {
            fieldRepository.deleteField(playerId)
        }
    }
}