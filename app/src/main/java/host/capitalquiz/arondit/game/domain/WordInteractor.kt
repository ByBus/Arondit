package host.capitalquiz.arondit.game.domain

import javax.inject.Inject

interface WordInteractor {

    suspend fun addWord(playerId: Long, word: Word): Long

    suspend fun deleteWord(wordId: Long)

    suspend fun updateWord(playerId: Long, word: Word)

    suspend fun word(wordId: Long): Word


    class Base @Inject constructor(private val wordRepository: WordRepository) : WordInteractor {
        override suspend fun addWord(playerId: Long, word: Word): Long =
            wordRepository.addWord(playerId, word)

        override suspend fun deleteWord(wordId: Long) = wordRepository.deleteWord(wordId)

        override suspend fun updateWord(playerId: Long, word: Word) =
            wordRepository.updateWord(playerId, word)

        override suspend fun word(wordId: Long): Word = wordRepository.selectWord(wordId)
    }
}