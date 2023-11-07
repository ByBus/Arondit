package host.capitalquiz.game.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import host.capitalquiz.core.db.WordData
import javax.inject.Inject

interface WordDataDataSource {
    fun read(): LiveData<WordData>

    fun save(word: WordData)

    fun isEmpty(): Boolean
    fun cachedValue(): WordData?


    class Base @Inject constructor(): WordDataDataSource {

        private val cachedWord = MutableLiveData<WordData>()
        override fun read(): LiveData<WordData> = cachedWord

        override fun save(word: WordData) {
           cachedWord.value = word
        }

        override fun isEmpty(): Boolean = cachedWord.value == null
        override fun cachedValue(): WordData? = cachedWord.value
    }
}