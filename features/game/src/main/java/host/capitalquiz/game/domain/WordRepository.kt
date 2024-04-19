package host.capitalquiz.game.domain

import kotlinx.coroutines.flow.Flow

interface WordRepository : CurrentWordRepository {

    suspend fun deleteWord(wordId: Long)
    suspend fun isWordExist(word: String): Boolean

}


interface CurrentWordRepository {
    fun readCache(): Flow<Word>

    suspend fun loadToCache(wordId: Long)

    suspend fun initCache(fieldId: Long)

    suspend fun updateCurrentWord(word: Word)

    suspend fun saveCacheToDb()

    fun cachedValue(): Word?
}
