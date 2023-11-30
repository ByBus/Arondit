package host.capitalquiz.game.data

import host.capitalquiz.core.db.FieldDao
import host.capitalquiz.core.db.FieldData
import host.capitalquiz.core.db.mappers.FieldDataMapperWithParameter
import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.FieldRepository
import host.capitalquiz.game.domain.GameRuleRepository
import host.capitalquiz.game.domain.GameRuleSimple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BaseFieldRepository @Inject constructor(
    private val fieldDao: FieldDao,
    private val mapper: FieldDataMapperWithParameter<GameRuleSimple, Field>,
    private val gameRuleRepository: GameRuleRepository,
) : FieldRepository {

    override fun allFieldsOfGame(gameId: Long): Flow<List<Field>> {
        return fieldDao.allFieldsOfGame(gameId).map { fields ->
            val rule = gameRuleRepository.gameRuleOfGame(gameId)
            fields.map { mapper.map(it, rule) }
        }
    }

    override suspend fun createField(field: Field, gameId: Long): Long {
        return fieldDao.insert(FieldData(field.color, gameId, field.playerId))
    }

    override suspend fun deleteField(filedId: Long) {
        fieldDao.deleteFieldById(filedId)
    }

    override suspend fun fieldsOfGame(gameId: Long): List<Field> {
        return withContext(Dispatchers.IO) {
            allFieldsOfGame(gameId).first()
        }
    }

    override suspend fun fieldById(fieldId: Long): Field {
        val fieldData = fieldDao.findFieldById(fieldId)
        val rule = gameRuleRepository.gameRuleOfGame(fieldData.field.gameId)
        return mapper.map(fieldData, rule)
    }

    override suspend fun findFieldsIdsWithPlayer(playerId: Long): List<Long> {
        return fieldDao.findFieldsIdsWithPlayer(playerId)
    }
}