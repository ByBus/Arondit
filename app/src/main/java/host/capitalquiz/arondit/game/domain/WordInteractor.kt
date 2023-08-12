package host.capitalquiz.arondit.game.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

interface WordInteractor {

    suspend fun addWord(playerId: Long, word: Word): Long

    suspend fun deleteWord(wordId: Long)

    suspend fun updateWord(playerId: Long, word: Word)

    suspend fun word(wordId: Long): Word

    suspend fun updateWord(word: String)

    suspend fun loadWordToCache(wordId: Long)

    suspend fun updateScore(letterPosition: Int)

    suspend fun updateMultiplier(value: Int)

    suspend fun saveCachedWord()

    suspend fun initCacheWithPlayer(playerId: Long)

    fun readCache(): LiveData<Word>

    class Base @Inject constructor(private val wordRepository: WordRepository) : WordInteractor {
        override suspend fun addWord(playerId: Long, word: Word): Long =
            wordRepository.addWord(playerId, word)

        override suspend fun deleteWord(wordId: Long) = wordRepository.deleteWord(wordId)

        override suspend fun updateWord(playerId: Long, word: Word) =
            wordRepository.updateWord(playerId, word)

        override suspend fun word(wordId: Long): Word = wordRepository.selectWord(wordId)

        override suspend fun updateWord(word: String) = wordRepository.updateCache(word)

        override fun readCache(): LiveData<Word> = wordRepository.readCache()

        override suspend fun loadWordToCache(wordId: Long) = wordRepository.loadToCache(wordId)

        override suspend fun updateScore(letterPosition: Int) =
            wordRepository.changeLetterScore(letterPosition)

        override suspend fun updateMultiplier(value: Int) = wordRepository.updateMultiplier(value)

        override suspend fun saveCachedWord() = wordRepository.saveCacheToDb()

        override suspend fun initCacheWithPlayer(playerId: Long) =
            wordRepository.initCache(playerId)
    }
}