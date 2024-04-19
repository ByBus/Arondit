package host.capitalquiz.game.data

import host.capitalquiz.core.db.WordDao
import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.mappers.WordDataMapper
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.WordRepository
import host.capitalquiz.game.domain.mappers.WordMapperWithParameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BaseWordRepository @Inject constructor(
    private val oneWordCache: WordDataDataSource,
    private val wordDao: WordDao,
    private val wordMapper: WordDataMapper<Word>,
    private val wordDataMapper: WordMapperWithParameter<Long, WordData>,
) : WordRepository {

    override suspend fun deleteWord(wordId: Long) = wordDao.deleteWordById(wordId)

    override suspend fun isWordExist(word: String): Boolean {
        val oldWord = oneWordCache.cachedValue() ?: return false
        return wordDao.isWordExist(oldWord.fieldId, word, oldWord.id)
    }

    override suspend fun loadToCache(wordId: Long) {
        if (oneWordCache.isEmpty()) {
            val word = wordDao.selectWordById(wordId)
            oneWordCache.save(word)
        }
    }

    override fun readCache(): Flow<Word> = oneWordCache.read().map {
        it?.map(wordMapper) ?: Word("")
    }

    override suspend fun initCache(fieldId: Long) {
        if (oneWordCache.isEmpty()) {
            oneWordCache.save(WordData("", fieldId = fieldId))
        }
    }

    override suspend fun updateCurrentWord(word: Word) {
        val field = oneWordCache.cachedValue()?.fieldId ?: 0
        oneWordCache.save(wordDataMapper.map(word, field))
    }

    override suspend fun saveCacheToDb() {
        oneWordCache.read().value?.let {
            wordDao.insertOrUpdate(it)
        }
    }

    override fun cachedValue(): Word? = oneWordCache.cachedValue()?.map(wordMapper)
}