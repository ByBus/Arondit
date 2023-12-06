package host.capitalquiz.arondit.implementation

import host.capitalquiz.core.interfaces.DeleteFieldUseCase
import host.capitalquiz.game.domain.FieldRepository
import host.capitalquiz.game.domain.PlayerRepository
import javax.inject.Inject

class BaseDeleteFieldUseCase @Inject constructor(
    private val fieldRepository: FieldRepository,
    private val playerRepository: PlayerRepository,
) : DeleteFieldUseCase {
    override suspend fun invoke(fieldId: Long) {
        val field = fieldRepository.fieldById(fieldId)
        val playerId = field.playerId
        val numberOfGamesWithPlayer = fieldRepository.findFieldsIdsWithPlayer(playerId).count()
        fieldRepository.deleteField(fieldId)
        if (numberOfGamesWithPlayer == 1) {
            playerRepository.deletePlayer(playerId)
        }
    }
}