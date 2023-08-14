package host.capitalquiz.arondit.game.data.glossary

import host.capitalquiz.arondit.game.domain.WordDefinition
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import retrofit2.Response
import javax.inject.Inject

interface HtmlParser {
    fun parse(htmlResult: Response<String>): WordDefinition

    class OzhegovSlovarOnlineComParser @Inject constructor(): HtmlParser {
        private val lowPriorityGlossary = "Словарь синонимов"

        override fun parse(htmlResult: Response<String>): WordDefinition {
            return if (htmlResult.isSuccessful) {
                val htmlDocument = Jsoup.parse(htmlResult.body().toString())
                val foundElements: Elements = htmlDocument.select(".search-result")

                if (foundElements.isEmpty()) throw IllegalStateException()
                val element = foundElements.findOtherThan(lowPriorityGlossary)!!

                val wordElement = element.select("h3 a")
                val word = wordElement.text()
                val url = wordElement.attr("abs:href")
                val glossaryTitle = element.select(".search-link").text()
                val definition = element.select(".truncate").text()
                WordDefinition.Base(word, glossaryTitle, definition, url)
            } else {
                WordDefinition.NotFound
            }
        }

        private fun Elements.findOtherThan(glossaryName: String): Element? {
            return firstOrNull { it.select(".search-link").text() != glossaryName } ?: first()
        }
    }
}