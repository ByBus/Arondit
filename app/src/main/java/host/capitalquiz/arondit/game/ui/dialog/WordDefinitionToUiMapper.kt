package host.capitalquiz.arondit.game.ui.dialog

import android.net.Uri
import host.capitalquiz.arondit.game.domain.WordDefinitionMapper
import javax.inject.Inject

class WordDefinitionToUiMapper @Inject constructor() : WordDefinitionMapper<WordDefinitionUi> {
    override fun invoke(
        word: String,
        glossary: String,
        definition: String,
        articleUrl: String?,
    ): WordDefinitionUi {
        val url = articleUrl?.let { Uri.parse(it) }
        return WordDefinitionUi.Base(word, glossary, definition, url)
    }

    override fun invoke(isEmpty: Boolean): WordDefinitionUi {
        return if (isEmpty) WordDefinitionUi.Empty else WordDefinitionUi.NoDefinition
    }
}