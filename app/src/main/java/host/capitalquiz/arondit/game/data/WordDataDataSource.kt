package host.capitalquiz.arondit.game.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import host.capitalquiz.arondit.core.db.WordData
import javax.inject.Inject

interface WordDataDataSource {
    fun read(): LiveData<WordData>

    fun save(word: WordData)

    fun isEmpty(): Boolean


    class Base @Inject constructor(): WordDataDataSource {

        private val cachedWord = MutableLiveData<WordData>()
        override fun read(): LiveData<WordData> = cachedWord

        override fun save(word: WordData) {
           cachedWord.value = word
        }

        override fun isEmpty(): Boolean = cachedWord.value == null
    }
}