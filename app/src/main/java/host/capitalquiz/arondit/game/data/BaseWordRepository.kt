package host.capitalquiz.arondit.game.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import host.capitalquiz.arondit.core.db.WordDao
import host.capitalquiz.arondit.core.db.WordData
import host.capitalquiz.arondit.core.db.WordDataMapper
import host.capitalquiz.arondit.game.domain.Word
import host.capitalquiz.arondit.game.domain.WordMapperWithParameter
import host.capitalquiz.arondit.game.domain.WordRepository
import javax.inject.Inject

class BaseWordRepository @Inject constructor(
    private val oneWordCache: WordDataDataSource,
    private val wordDao: WordDao,
    private val wordMapper: WordDataMapper<Word>,
    private val wordDataMapper: WordMapperWithParameter<Long, Word, WordData>,
) : WordRepository {

    override suspend fun deleteWord(wordId: Long) = wordDao.deleteWordById(wordId)
    override suspend fun isWordExist(word: String): Boolean {
        val playerId = oneWordCache.cachedValue()?.playerId ?: return false
        return wordDao.isWordExist(playerId, word)
    }

    override suspend fun loadToCache(wordId: Long) {
        if (oneWordCache.isEmpty()) {
            val word = wordDao.selectWordById(wordId)
            oneWordCache.save(word)
        }
    }

    override fun readCache(): LiveData<Word> = oneWordCache.read().map { it.map(wordMapper) }

    override suspend fun initCache(playerId: Long) {
        if (oneWordCache.isEmpty()) {
            oneWordCache.save(WordData("", playerId = playerId))
        }
    }

    override suspend fun updateCurrentWord(word: Word) {
        val playerId = oneWordCache.cachedValue()?.playerId ?: 0
        oneWordCache.save(wordDataMapper.map(word, playerId))
    }

    override suspend fun saveCacheToDb() {
        oneWordCache.read().value?.let {
            wordDao.insertOrUpdate(it)
        }
    }

    override fun cachedValue(): Word? = oneWordCache.cachedValue()?.map(wordMapper)
}