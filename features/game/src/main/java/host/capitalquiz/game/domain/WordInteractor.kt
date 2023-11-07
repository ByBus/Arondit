package host.capitalquiz.game.domain

import androidx.lifecycle.LiveData
import host.capitalquiz.game.data.StringFormatter
import javax.inject.Inject

interface WordInteractor {

    suspend fun deleteWord(wordId: Long)

    suspend fun updateWord(word: String)

    suspend fun loadWordToCache(wordId: Long)

    suspend fun cycleLetterScore(letterPosition: Int)

    suspend fun switchLetterAsterisk(letterPosition: Int)

    suspend fun updateMultiplier(value: Int)

    suspend fun updateExtraPoints(value: Boolean)

    suspend fun saveWord(): Boolean

    suspend fun checkIfWordAlreadyExists(word: String): Boolean

    suspend fun initCacheWithPlayer(playerId: Long)

    fun loadWord(): LiveData<Word>

    suspend fun findDefinition(word: String): WordDefinition

    class Base @Inject constructor(
        private val wordRepository: WordRepository,
        private val definitionRepository: DefinitionRepository,
        private val wordFormatter: StringFormatter,
        private val bonusUpdater: WordToWordMapper
    ) : WordInteractor {

        override suspend fun deleteWord(wordId: Long) = wordRepository.deleteWord(wordId)


        override suspend fun updateWord(word: String) {
            wordRepository.cachedValue()?.let {
                val newWord = bonusUpdater.map(it, wordFormatter.format(word))
                wordRepository.updateCurrentWord(newWord)
            }
        }

        override fun loadWord(): LiveData<Word> = wordRepository.readCache()

        override suspend fun findDefinition(word: String): WordDefinition =
            definitionRepository.findDefinition(word)

        override suspend fun loadWordToCache(wordId: Long) = wordRepository.loadToCache(wordId)

        override suspend fun cycleLetterScore(letterPosition: Int) {
            wordRepository.cachedValue()?.let {
                val bonuses = it.letterBonuses.toMutableList()
                bonuses[letterPosition] =
                    if (bonuses[letterPosition] == 3) 1 else bonuses[letterPosition] + 1
                wordRepository.updateCurrentWord(it.copy(letterBonuses = bonuses))
            }
        }

        override suspend fun switchLetterAsterisk(letterPosition: Int) {
            wordRepository.cachedValue()?.let {
                val bonuses = it.letterBonuses.toMutableList()
                bonuses[letterPosition] = if (bonuses[letterPosition] == 0) 1 else 0
                wordRepository.updateCurrentWord(it.copy(letterBonuses = bonuses))
            }
        }

        override suspend fun updateMultiplier(value: Int) {
            wordRepository.cachedValue()?.let {
                wordRepository.updateCurrentWord(it.copy(multiplier = value))
            }
        }

        override suspend fun updateExtraPoints(value: Boolean) {
            wordRepository.cachedValue()?.let {
                wordRepository.updateCurrentWord(it.copy(hasExtraPoints = value))
            }
        }

        override suspend fun saveWord(): Boolean {
            val allowSave = (wordRepository.cachedValue()?.let {
                checkIfWordAlreadyExists(it.word)
            } ?: false).not()
            if (allowSave) {
                wordRepository.saveCacheToDb()
            }
            return allowSave
        }

        override suspend fun checkIfWordAlreadyExists(word: String): Boolean =
            wordRepository.isWordExist(word)

        override suspend fun initCacheWithPlayer(playerId: Long) =
            wordRepository.initCache(playerId)

    }
}