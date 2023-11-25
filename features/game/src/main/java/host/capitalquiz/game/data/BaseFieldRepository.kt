package host.capitalquiz.game.data

import host.capitalquiz.core.db.FieldDao
import host.capitalquiz.core.db.FieldData
import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.mappers.FieldDataMapperWithParameter
import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.FieldRepository
import host.capitalquiz.game.domain.GameRuleRepository
import host.capitalquiz.game.domain.GameRuleSimple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BaseFieldRepository @Inject constructor(
    private val fieldDao: FieldDao,
    private val mapper: FieldDataMapperWithParameter<GameRuleSimple, Field>,
    private val gameRuleRepository: GameRuleRepository,
) : FieldRepository {

    override fun allFieldsOfGame(gameId: Long): Flow<List<Field>> {
        return fieldDao.allPlayerByGameId(gameId).map { fields ->
            val rule = gameRuleRepository.gameRuleOfGame(gameId)
            fields.map { mapper.map(it, rule) }
        }
    }

    override suspend fun createPlayer(field: Field, gameId: Long): Long {
        val playerId = fieldDao.insert(PlayerData(field.name))
        return fieldDao.insert(FieldData(field.color, gameId, playerId))
    }

    override suspend fun deleteField(filedId: Long) {
        fieldDao.deleteFieldById(filedId)
    }
}