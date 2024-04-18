package host.capitalquiz.game.data.glossary

import host.capitalquiz.game.di.GufoMeGlossary
import host.capitalquiz.game.di.OzhegovGlossary
import host.capitalquiz.game.domain.WordDefinition
import javax.inject.Inject

interface Glossary {

    fun next(glossary: Glossary): Glossary

    suspend fun requestDefinition(word: String): WordDefinition

    abstract class AbstractGlossary(private val glossaryApi: GlossaryApi, private val parser: HtmlParser) : Glossary {
        private var nextGlossary: Glossary = EmptyGlossary
        abstract fun urlForWord(word: String): String

        override fun next(glossary: Glossary): Glossary {
            nextGlossary = glossary
            return glossary
        }

        override suspend fun requestDefinition(word: String): WordDefinition {
            if (word.isBlank()) return WordDefinition.NotFound
            val result = try {
                val htmlResult = glossaryApi.requestWord(urlForWord(word))
                val definition = parser.parse(htmlResult)
                definition.takeIf { it.isDefinitionOf(word) } ?: WordDefinition.Empty
            } catch (e: Exception) {
                e.printStackTrace()
                WordDefinition.NotFound
            }
            return if (result.isSuccessful || nextGlossary == EmptyGlossary)
                result
            else
                nextGlossary.requestDefinition(word)
        }
    }

    class OzhegovSlovarOnlineCom @Inject constructor(
        glossaryApi: GlossaryApi,
        @OzhegovGlossary
        parser: HtmlParser,
    ) : AbstractGlossary(glossaryApi, parser) {
        private val baseUrl = "https://ozhegov.slovaronline.com/search"

        override fun urlForWord(word: String): String = "$baseUrl?s=$word"
    }

    class GufoMe @Inject constructor(
        glossaryApi: GlossaryApi,
        @GufoMeGlossary
        parser: HtmlParser,
    ) : AbstractGlossary(glossaryApi, parser){
        private val baseUrl = "https://gufo.me/search"

        override fun urlForWord(word: String): String = "$baseUrl?term=$word"
    }

    object EmptyGlossary: Glossary {
        override fun next(glossary: Glossary): Glossary = this

        override suspend fun requestDefinition(word: String): WordDefinition = WordDefinition.Empty
    }
}

