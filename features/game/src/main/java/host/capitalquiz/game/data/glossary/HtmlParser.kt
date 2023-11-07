package host.capitalquiz.game.data.glossary

import host.capitalquiz.game.domain.WordDefinition
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import retrofit2.Response
import javax.inject.Inject

interface HtmlParser {
    fun parse(htmlResult: Response<String>): WordDefinition

    abstract class AbstractParser: HtmlParser {
        override fun parse(htmlResult: Response<String>): WordDefinition {
            return if (htmlResult.isSuccessful) {
                parseString(htmlResult.body().toString())
            } else {
                WordDefinition.NotFound
            }
        }

        protected abstract fun parseString(html: String): WordDefinition
    }

    class OzhegovSlovarOnlineComParser @Inject constructor() : AbstractParser() {
        private val glossaryPriority = mapOf(
            "Толковый словарь русского языка" to 50,
            "Словарь современных географических названий" to 40,
            "Города России" to 30,
            "Энциклопедия моды и одежды" to 20,
            "Словарь синонимов" to 10
        )

        override fun parseString(html: String): WordDefinition {
            val htmlDocument = Jsoup.parse(html)
            val foundElements: Elements = htmlDocument.select(".search-result")

            if (foundElements.isEmpty()) throw IllegalStateException()
            val element = foundElements.sortedWith(elementsComparator).first()

            val wordElement = element.select("h3 a")
            val word = wordElement.text()
            val url = wordElement.attr("abs:href")
            val glossaryTitle = element.select(".search-link").text()
            val definition = element.select(".truncate").text()
            return WordDefinition.Base(word, glossaryTitle, definition, url)
        }

        private val elementsComparator = Comparator<Element> { o1, o2 ->
            o2.priority() - o1.priority()
        }
        private fun Element.priority(): Int = glossaryPriority[select(".search-link").text()] ?: 0
    }

    class GufoMeParser @Inject constructor() : AbstractParser() {
        override fun parseString(html: String): WordDefinition {
            val htmlDocument = Jsoup.parse(html)
            val foundElements: Elements = htmlDocument.select("#dictionary-search ol>li")
            if (foundElements.isEmpty()) throw IllegalStateException()
            val element = foundElements[0]

            val wordElement = element.select("a")
            val word = wordElement.text()
            val url = wordElement.attr("href")
            val baseUrl = "https://gufo.me".takeIf { url.isNotBlank() } ?: ""

            val glossaryTitle = element.select("span.small").text()
            val definition = element.select("p").text()
            return WordDefinition.Base(word, glossaryTitle, definition, baseUrl + url)
        }
    }
}