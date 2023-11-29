package host.capitalquiz.game.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FieldInteractor {

    fun findAllFieldsOfGame(gameId: Long): Flow<List<Field>>

    suspend fun createField(field: Field, gameId: Long): Boolean

    suspend fun deleteField(fieldId: Long)

    suspend fun findAllPlayersWhoIsNotPlayingYet(gameId: Long): List<Player>
    suspend fun renamePlayer(newName: String, playerId: Long): Boolean

    class Base @Inject constructor(
        private val fieldRepository: FieldRepository,
    ) : FieldInteractor {
        override fun findAllFieldsOfGame(gameId: Long): Flow<List<Field>> {
            return fieldRepository.allFieldsOfGame(gameId)
        }

        override suspend fun createField(field: Field, gameId: Long): Boolean {
            val allPlayersNames = fieldRepository.allPlayers().map { it.name }
            val nameTaken = field.name.isNotBlank() && allPlayersNames.any {
                it.equals(field.name, true)
            }
            return if (nameTaken)
                false
            else {
                val playerId = field.playerId.takeIf { field.hasPlayerId() }
                    ?: fieldRepository.createPlayerWithName(field.name)
                fieldRepository.createField(field.copy(playerId = playerId), gameId)
                true
            }
        }

        override suspend fun deleteField(fieldId: Long) {
            fieldRepository.deleteField(fieldId)
        }

        override suspend fun findAllPlayersWhoIsNotPlayingYet(gameId: Long): List<Player> {
            val fields = fieldRepository.fieldsOfGame(gameId)
            val nowPlayingPlayers = fields.map { it.name }
            val allPlayers = fieldRepository.allPlayers()
            return allPlayers.filterNot { it.name in nowPlayingPlayers }.sortedBy { it.name }
        }

        override suspend fun renamePlayer(newName: String, playerId: Long): Boolean {
            val playerWithSameNameExist = fieldRepository.allPlayers().any {
                it.id != playerId && it.name.equals(newName, true)
            }
            return if (playerWithSameNameExist)
                false
            else {
                fieldRepository.renamePlayer(newName, playerId)
                true
            }
        }
    }
}