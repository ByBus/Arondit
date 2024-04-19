package host.capitalquiz.game.data

import host.capitalquiz.core.db.FieldDao
import host.capitalquiz.core.db.FieldData
import host.capitalquiz.core.db.FieldWithPlayerAndWordsData
import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.mappers.FieldDataMapperWithParameter
import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.GameRuleRepository
import host.capitalquiz.game.domain.GameRuleSimple
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseFieldRepositoryTest {
    private lateinit var mapper: FakeFieldDataMapper
    private lateinit var gameRuleRepository: FakeGameRuleRepository
    private lateinit var fieldDao: FakeFieldDao

    @Before
    fun setUp() {
        mapper = FakeFieldDataMapper()
        gameRuleRepository = FakeGameRuleRepository()
        fieldDao = FakeFieldDao()
    }

    @Test
    fun `correct field creation`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        val fieldRepository = BaseFieldRepository(fieldDao, mapper, gameRuleRepository, dispatcher)

        val newField = Field(1L, "NoName", 0xFFF, 0, emptyList(), 2L)
        val id = fieldRepository.createField(newField, 100L)

        val actual = fieldDao.fields.first { it.id == id }
        val expected = FieldData(0xFFF, 100L, 2L)

        assertEquals(expected, actual)
        assertEquals(0, mapper.mapExecutionsCounter)
    }

    @Test
    fun `get field by id`() = runTest {
        gameRuleRepository.cachedGameRule = GameRuleSimple(0, emptyMap())
        fieldDao.fields.add(FieldData(0xBBB, 2, 0).also { it.id = 30 })
        val dispatcher = StandardTestDispatcher(testScheduler)

        val fieldRepository = BaseFieldRepository(fieldDao, mapper, gameRuleRepository, dispatcher)

        val expected = Field(30, "Player", 0xBBB, 0, emptyList(), 0)
        val actual = fieldRepository.fieldById(30)

        assertEquals(expected, actual)
        assertEquals(1, mapper.mapExecutionsCounter)
    }

    @Test
    fun `remove field test`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        val fieldRepository = BaseFieldRepository(fieldDao, mapper, gameRuleRepository, dispatcher)

        val newField = Field(1L, "NoName", 0xFFF, 0, emptyList(), 2L)
        val fieldId = fieldRepository.createField(newField, 100L)

        val actual = fieldDao.fields.first { it.id == fieldId }
        val expected = FieldData(0xFFF, 100L, 2L)

        assertEquals(expected, actual)
        assertEquals(0, mapper.mapExecutionsCounter)

        fieldRepository.deleteField(fieldId)
        assert(fieldDao.fields.isEmpty())
        assertEquals(0, mapper.mapExecutionsCounter)
    }

    @Test
    fun `get all fields of the game`() = runTest {
        gameRuleRepository.cachedGameRule = GameRuleSimple(0, emptyMap())
        fieldDao.fields.addAll(
            listOf(
                FieldData(0xFFF, 1, 0).also { it.id = 10 },
                FieldData(0xAAA, 1, 0).also { it.id = 20 },
                FieldData(0xBBB, 2, 0).also { it.id = 30 },
                FieldData(0xBBB, 3, 0).also { it.id = 40 },
            )
        )
        val dispatcher = StandardTestDispatcher(testScheduler)

        val fieldRepository = BaseFieldRepository(fieldDao, mapper, gameRuleRepository, dispatcher)

        val actual = fieldRepository.fieldsOfGame(1L)

        val expected = listOf(
            Field(10, "Player", 0xFFF, 0, emptyList(), 0),
            Field(20, "Player", 0xAAA, 0, emptyList(), 0)
        )

        assertEquals(expected, actual)
        assertEquals(2, mapper.mapExecutionsCounter)
    }

    @Test
    fun `get identifiers of the fields in which player is participating`() = runTest {
        gameRuleRepository.cachedGameRule = GameRuleSimple(0, emptyMap())
        fieldDao.fields.addAll(
            listOf(
                FieldData(0xFFF, 1, 1).also { it.id = 10 },
                FieldData(0xAAA, 1, 2).also { it.id = 20 },
                FieldData(0xBBB, 2, 2).also { it.id = 30 },
                FieldData(0xBBB, 3, 3).also { it.id = 40 },
            )
        )
        val dispatcher = StandardTestDispatcher(testScheduler)

        val fieldRepository = BaseFieldRepository(fieldDao, mapper, gameRuleRepository, dispatcher)

        val expected = listOf(20L, 30L)
        val actual = fieldRepository.findFieldsIdsWithPlayer(2)

        assertEquals(expected, actual)
        assertEquals(0, mapper.mapExecutionsCounter)
    }
}


class FakeFieldDataMapper : FieldDataMapperWithParameter<GameRuleSimple, Field> {
    var mapExecutionsCounter = 0;
    override fun invoke(player: PlayerData, words: List<WordData>, field: FieldData): Field {
        throw IllegalAccessError("Method was called directly")
    }

    override fun map(field: FieldWithPlayerAndWordsData, param: GameRuleSimple): Field {
        mapExecutionsCounter++;
        return Field(
            field.field.id,
            field.player.name,
            field.field.color,
            0,
            emptyList(),
            field.player.id
        )
    }
}

class FakeGameRuleRepository : GameRuleRepository {
    var cachedGameRule: GameRuleSimple? = null

    override suspend fun gameRuleOfGame(gameId: Long): GameRuleSimple = cachedGameRule!!

    override fun readLastRule(): GameRuleSimple = cachedGameRule!!

}

class FakeFieldDao : FieldDao {
    val fields = mutableListOf<FieldData>()

    override suspend fun insert(field: FieldData): Long {
        fields.add(field)
        return field.id
    }

    override fun allFieldsOfGame(gameId: Long): Flow<List<FieldWithPlayerAndWordsData>> {
        return flow {
            emit(fields.filter { it.gameId == gameId }.map { fieldData ->
                FieldWithPlayerAndWordsData(
                    fieldData,
                    PlayerData("Player").also { it.id = fieldData.playerId },
                    emptyList()
                )
            })
        }
    }

    override suspend fun deleteFieldById(filedId: Long) {
        fields.removeIf { it.id == filedId }
    }

    override suspend fun findFieldById(fieldId: Long): FieldWithPlayerAndWordsData {
        return fields.first {
            it.id == fieldId
        }.let { fieldData ->
            FieldWithPlayerAndWordsData(fieldData, PlayerData("Player"), emptyList())
        }
    }

    override suspend fun findFieldsIdsWithPlayer(playerId: Long): List<Long> {
        return fields.filter { it.playerId == playerId }.map { it.id }
    }

    override suspend fun findAllFieldsIdsOfGame(gameId: Long): List<Long> {
        return fields.filter { it.gameId == gameId }.map { it.id }
    }
}