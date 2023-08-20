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
    private val wordBonusMapper: WordDataToWordDataMapper,
    private val wordFormatter: StringFormatter
) : WordRepository {
    override suspend fun addWord(playerId: Long, word: Word): Long {
        return wordDao.insert(wordDataMapper.map(word, playerId))
    }

    override suspend fun deleteWord(wordId: Long) {
        wordDao.deleteWordById(wordId)
    }

    override suspend fun selectWord(wordId: Long): Word {
        return wordDao.selectWordById(wordId).map(wordMapper)
    }

    override suspend fun updateWord(playerId: Long, word: Word) {
        wordDao.updateWord(wordDataMapper.map(word, playerId))
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

    override suspend fun updateCache(word: String) {
        oneWordCache.read().value?.let { oldWordData ->
            oneWordCache.save(wordBonusMapper.map(oldWordData, wordFormatter.format(word)))
        }
    }

    override suspend fun updateMultiplier(value: Int) {
        oneWordCache.read().value?.let {
            oneWordCache.save(it.deepCopy(multiplier = value))
        }
    }

    override suspend fun changeLetterScore(position: Int) {
        oneWordCache.read().value?.let {
            val bonuses = it.letterBonuses.toMutableList()
            bonuses[position] = if (bonuses[position] == 3) 1 else bonuses[position] + 1
            oneWordCache.save(it.deepCopy(letterBonuses = bonuses))
        }
    }

    override suspend fun saveCacheToDb() {
        oneWordCache.read().value?.let {
            wordDao.insertOrUpdate(it)
        }
    }
}