package host.capitalquiz.arondit.game.domain

interface WordRepository {

    suspend fun addWord(playerId: Long, word: Word): Long

    suspend fun deleteWord(wordId: Long)

    suspend fun selectWord(wordId: Long): Word

    suspend fun updateWord(playerId: Long, word: Word)

}