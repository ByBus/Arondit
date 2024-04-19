package host.capitalquiz.game.data

import host.capitalquiz.core.db.WordData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface WordDataDataSource {
    fun read(): StateFlow<WordData?>

    fun save(word: WordData)

    fun isEmpty(): Boolean
    fun cachedValue(): WordData?


    class Base @Inject constructor(): WordDataDataSource {

        private val cachedWord = MutableStateFlow<WordData?>(null)
        override fun read() = cachedWord.asStateFlow()

        override fun save(word: WordData) {
           cachedWord.value = word
        }

        override fun isEmpty(): Boolean = cachedWord.value == null
        override fun cachedValue(): WordData? = cachedWord.value
    }
}