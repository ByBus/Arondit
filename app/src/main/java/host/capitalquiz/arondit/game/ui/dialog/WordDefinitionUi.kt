package host.capitalquiz.arondit.game.ui.dialog

import android.net.Uri
import host.capitalquiz.arondit.core.ui.view.GlossaryView

interface WordDefinitionUi {
    fun update(glossaryView: GlossaryView)
    class Base(
        val word: String,
        val glossaryTitle: String,
        val definition: String,
        val webSiteUrl: Uri?,
    ): WordDefinitionUi {
        override fun update(glossaryView: GlossaryView) {
            glossaryView.setWithAnimation(word, glossaryTitle, definition)
        }
    }

    object NoDefinition: WordDefinitionUi {
        override fun update(glossaryView: GlossaryView) {
            glossaryView.setWithAnimation("", "", "")
        }
    }

    object Empty: WordDefinitionUi {
        override fun update(glossaryView: GlossaryView) { }
    }
}
