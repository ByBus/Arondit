package host.capitalquiz.arondit.game.domain

import androidx.lifecycle.LiveData

interface WordRepository : CurrentWordRepository {

    suspend fun deleteWord(wordId: Long)
    suspend fun isWordExist(word: String): Boolean

}


interface CurrentWordRepository {
    fun readCache(): LiveData<Word>

    suspend fun loadToCache(wordId: Long)

    suspend fun initCache(playerId: Long)

    suspend fun updateCurrentWord(word: Word)

    suspend fun saveCacheToDb()

    fun cachedValue(): Word?
}
