package host.capitalquiz.arondit.game.data

import host.capitalquiz.arondit.core.db.WordDao
import host.capitalquiz.arondit.core.db.WordData
import host.capitalquiz.arondit.core.db.WordDataMapper
import host.capitalquiz.arondit.game.domain.Word
import host.capitalquiz.arondit.game.domain.WordMapperWithId
import host.capitalquiz.arondit.game.domain.WordRepository
import javax.inject.Inject

class BaseWordRepository @Inject constructor(
    private val wordDao: WordDao,
    private val wordUiMapper: WordDataMapper<Word>,
    private val wordDataMapper: WordMapperWithId<Long, WordData>,
) : WordRepository {
    override suspend fun addWord(playerId: Long, word: Word): Long {
        return wordDao.insert(word.map(wordDataMapper.apply { additionalId = playerId }))
    }

    override suspend fun deleteWord(wordId: Long) {
        wordDao.deleteWordById(wordId)
    }

    override suspend fun selectWord(wordId: Long): Word {
        return wordDao.selectWordById(wordId).map(wordUiMapper)
    }

    override suspend fun updateWord(playerId: Long, word: Word) {
        wordDao.updateWord(word.map(wordDataMapper.apply { additionalId = playerId }))
    }
}