package host.capitalquiz.arondit.game.domain

interface WordDefinition {

    fun <R> map(mapper: WordDefinitionMapper<R>): R

    fun equalTo(word: String): Boolean
    data class Base(
        val word: String,
        val glossary: String,
        val definition: String,
        val articleUrl: String? = null,
    ) : WordDefinition {
        override fun <R> map(mapper: WordDefinitionMapper<R>): R =
            mapper(word, glossary, definition, articleUrl)

        override fun equalTo(word: String): Boolean = this.word.equals(word, true)
    }


    object Empty : WordDefinition {
        override fun <R> map(mapper: WordDefinitionMapper<R>): R = mapper.invoke(isEmpty = true)

        override fun equalTo(word: String): Boolean = false

    }

    object NotFound : WordDefinition {
        override fun <R> map(mapper: WordDefinitionMapper<R>): R = mapper.invoke(isEmpty = false)

        override fun equalTo(word: String): Boolean = word.isBlank()
    }
}