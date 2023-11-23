package host.capitalquiz.game.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import host.capitalquiz.core.db.WordDao
import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.mappers.WordDataMapper
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.WordRepository
import host.capitalquiz.game.domain.mappers.WordMapperWithParameter
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
        return wordDao.isWordExist(oldWord.playerId, word, oldWord.id)
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