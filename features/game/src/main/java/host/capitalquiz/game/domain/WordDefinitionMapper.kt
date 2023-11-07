package host.capitalquiz.game.domain

interface WordDefinitionMapper<R> {
    operator fun invoke(word: String, glossary: String, definition: String, articleUrl: String?): R

    operator fun invoke(isEmpty: Boolean): R
}