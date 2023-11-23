package host.capitalquiz.game.ui.dialog.mappers

import host.capitalquiz.game.domain.mappers.WordDefinitionMapper
import host.capitalquiz.game.ui.dialog.WordDefinitionUi
import javax.inject.Inject

class WordDefinitionToUiMapper @Inject constructor() : WordDefinitionMapper<WordDefinitionUi> {
    override fun invoke(
        word: String,
        glossary: String,
        definition: String,
        articleUrl: String?,
    ): WordDefinitionUi {
        return WordDefinitionUi.Base(word, glossary, definition, articleUrl)
    }

    override fun invoke(isEmpty: Boolean): WordDefinitionUi {
        return if (isEmpty) WordDefinitionUi.Empty else WordDefinitionUi.NoDefinition
    }
}