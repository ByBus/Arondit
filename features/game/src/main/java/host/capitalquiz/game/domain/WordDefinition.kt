package host.capitalquiz.game.domain

import host.capitalquiz.game.domain.mappers.WordDefinitionMapper

interface WordDefinition {

    fun <R> map(mapper: WordDefinitionMapper<R>): R

    val isSuccessful: Boolean

    fun isDefinitionOf(word: String): Boolean
    data class Base(
        val word: String,
        val glossary: String,
        val definition: String,
        val articleUrl: String? = null,
    ) : WordDefinition {
        override fun <R> map(mapper: WordDefinitionMapper<R>): R =
            mapper(word, glossary, definition, articleUrl)

        override fun isDefinitionOf(word: String): Boolean = this.word.equals(word, true)

        override val isSuccessful = true
    }


    object Empty : WordDefinition {
        override fun <R> map(mapper: WordDefinitionMapper<R>): R = mapper.invoke(isEmpty = true)

        override fun isDefinitionOf(word: String): Boolean = false

        override val isSuccessful = false
    }

    object NotFound : WordDefinition {
        override fun <R> map(mapper: WordDefinitionMapper<R>): R = mapper.invoke(isEmpty = false)

        override fun isDefinitionOf(word: String): Boolean = word.isBlank()

        override val isSuccessful = false
    }
}