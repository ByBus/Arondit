package host.capitalquiz.game.data

import host.capitalquiz.core.db.WordDao
import host.capitalquiz.core.db.WordData
import host.capitalquiz.game.data.mappers.WordDataToWordMapper
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.mappers.WordMapperWithParameter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BaseWordRepositoryTest {
    private lateinit var fakeWordCache: FakeWordCache
    private lateinit var fakeWordDao: FakeWordDao
    private val wordDataToWordMapper = WordDataToWordMapper() // too simple to fake
    private lateinit var fakeWordToWordDataMapper: FakeWordToWordDataMapper

    @Before
    fun setUp() {
        fakeWordCache = FakeWordCache()
        fakeWordDao = FakeWordDao()
        fakeWordToWordDataMapper = FakeWordToWordDataMapper()
    }

    @Test
    fun `load word from db to cache`() = runTest {
        fakeWordDao.words.add(WordData("WORD", emptyList(), 1, 10, 0).also { it.id = 100 })

        val wordRepository = createBaseWordRepository()

        wordRepository.loadToCache(wordId = 100)
        val actual = fakeWordCache.cachedWord
        val expected = WordData("WORD", emptyList(), 1, 10, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun `create empty word in cache for game field and save to db`() = runTest {
        val wordRepository = createBaseWordRepository()

        assert(fakeWordDao.words.isEmpty())
        assert(fakeWordCache.cachedWord == null)

        wordRepository.initCache(fieldId = 3L)

        assert(fakeWordCache.cachedWord?.fieldId == 3L)
        assert(fakeWordDao.words.isEmpty())

        wordRepository.saveCacheToDb()

        val actual = fakeWordDao.words.first()
        val expected = WordData("", fieldId = 3L)

        assertEquals(expected, actual)
    }

    @Test
    fun `update word in cache`() = runTest {
        val wordRepository = createBaseWordRepository()

        wordRepository.initCache(fieldId = 3L)

        var actual = fakeWordCache.cachedWord
        var expected = WordData("", fieldId = 3L)
        assertEquals(expected, actual)

        wordRepository.updateCurrentWord(Word("WORD"))

        actual = fakeWordCache.cachedWord
        expected = WordData("WORD", fieldId = 3L)
        assertEquals(expected, actual)
    }

    @Test
    fun `delete word from db`() = runTest {
        val wordRepository = createBaseWordRepository()
        fakeWordDao.words.addAll(
            listOf(
                WordData("WORD_1", fieldId = 1).also { it.id = 11 },
                WordData("WORD_2", fieldId = 2).also { it.id = 12 }
            )
        )

        wordRepository.deleteWord(wordId = 12)

        val actual = fakeWordDao.words
        val expected = listOf(WordData("WORD_1", fieldId = 1).also { it.id = 12 })
        assertEquals(expected, actual)
    }

    @Test
    fun `read word from empty cache`() = runTest {
        val wordRepository = createBaseWordRepository()
        var actual = wordRepository.cachedValue()
        assert(actual == null)

        actual = wordRepository.readCache().first()
        val expected = Word("")
        assertEquals(expected, actual)
    }

    private fun createBaseWordRepository() = BaseWordRepository(
        fakeWordCache,
        fakeWordDao,
        wordDataToWordMapper,
        fakeWordToWordDataMapper
    )
}

class FakeWordToWordDataMapper : WordMapperWithParameter<Long, WordData> {
    override fun map(word: Word, filedId: Long): WordData {
        return WordData(word.word, emptyList(), 1, filedId, 0)
    }

    override fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        extraPoints: Int,
    ): WordData {
        throw IllegalAccessError("Direct method call")
    }
}

class FakeWordDao(var wordExists: Boolean = false) : WordDao {
    val words = mutableListOf<WordData>()

    override suspend fun insertOrUpdate(word: WordData): Long {
        val oldWord = words.firstOrNull { it.id == word.id }
        if (oldWord != null) {
            word.id = oldWord.id
            words.remove(oldWord)
        }
        words.add(word)
        return word.id
    }

    override suspend fun deleteWordById(id: Long) {
        words.removeIf { it.id == id }
    }

    override suspend fun selectWordById(id: Long): WordData = words.first { it.id == id }

    override suspend fun isWordExist(fieldId: Long, word: String, wordId: Long): Boolean =
        wordExists

}

class FakeWordCache : WordDataDataSource {
    var cachedWord: WordData? = null

    override fun read(): StateFlow<WordData?> {
        return MutableStateFlow(cachedWord)
    }

    override fun save(word: WordData) {
        cachedWord = word
    }

    override fun isEmpty(): Boolean = cachedWord == null

    override fun cachedValue(): WordData? = cachedWord
}