package host.capitalquiz.arondit.game.domain

import androidx.lifecycle.LiveData

interface WordRepository: WordCache {

    suspend fun addWord(playerId: Long, word: Word): Long

    suspend fun deleteWord(wordId: Long)

    suspend fun selectWord(wordId: Long): Word

    suspend fun updateWord(playerId: Long, word: Word)

}


interface WordCache{
    fun readCache(): LiveData<Word>

    suspend fun loadToCache(wordId: Long)

    suspend fun initCache(playerId: Long)

    suspend fun updateCache(word: String)

    suspend fun updateMultiplier(value: Int)

    suspend fun changeLetterScore(position: Int)

    suspend fun saveCacheToDb()
}