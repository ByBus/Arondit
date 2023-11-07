package host.capitalquiz.arondit.game.ui.dialog

import host.capitalquiz.arondit.game.ui.GlossaryView

interface WordDefinitionUi {
    fun update(glossaryView: GlossaryView)
    class Base(
        val word: String,
        val glossaryTitle: String,
        val definition: String,
        val webSiteUrl: String?,
    ): WordDefinitionUi {
        override fun update(glossaryView: GlossaryView) {
            glossaryView.setWithAnimation(word, glossaryTitle, definition, webSiteUrl)
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
