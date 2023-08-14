package host.capitalquiz.arondit.game.data.glossary

import host.capitalquiz.arondit.game.domain.WordDefinition
import javax.inject.Inject

interface Glossary {
    fun urlForWord(word: String): String
    suspend fun requestDefinition(word: String): WordDefinition

    class OzhegovSlovarOnlineCom @Inject constructor(
        private val glossaryApi: GlossaryApi,
        private val parser: HtmlParser,
    ) : Glossary {
        private val baseUrl = "https://ozhegov.slovaronline.com/search"

        override fun urlForWord(word: String): String = "$baseUrl?s=$word"

        override suspend fun requestDefinition(word: String): WordDefinition {
            return try {
                val htmlResult = glossaryApi.requestWord(urlForWord(word))
                val definition = parser.parse(htmlResult)
                definition.takeIf { it.equalTo(word) } ?: WordDefinition.Empty
            } catch (e: Exception) {
                e.printStackTrace()
                WordDefinition.NotFound
            }
        }
    }
}

