package host.capitalquiz.arondit.game.data.glossary

import host.capitalquiz.arondit.game.domain.WordDefinition
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import retrofit2.Response
import javax.inject.Inject

interface HtmlParser {
    fun parse(htmlResult: Response<String>): WordDefinition

    class OzhegovSlovarOnlineComParser @Inject constructor() : HtmlParser {
        private val glossaryPriority = mapOf(
            "Толковый словарь русского языка" to 50,
            "Словарь современных географических названий" to 40,
            "Города России" to 30,
            "Энциклопедия моды и одежды" to 20,
            "Словарь синонимов" to 10
        )

        override fun parse(htmlResult: Response<String>): WordDefinition {
            return if (htmlResult.isSuccessful) {
                val htmlDocument = Jsoup.parse(htmlResult.body().toString())
                val foundElements: Elements = htmlDocument.select(".search-result")

                if (foundElements.isEmpty()) throw IllegalStateException()
                val element = foundElements.sortedWith(elementsComparator).first()

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

        private val elementsComparator = Comparator<Element> { o1, o2 ->
            o2.priority() - o1.priority()
        }

        private fun Element.priority(): Int = glossaryPriority[select(".search-link").text()] ?: 0

    }
}