package host.capitalquiz.game.data.glossary

import host.capitalquiz.game.domain.WordDefinition
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response


class HtmlParserTest {

    @Test
    fun `correct Ozhegov Slovar html response parsing`() {
        val ozhegovParser = HtmlParser.OzhegovSlovarOnlineComParser()
        val htmlResponse = """
            <div class="search-result highlight">
                <h3>
                    <a href="https://ozhegov.slovaronline.com/31899-SKAZKA" rel="dns-prefetch" title="СКАЗКА в Толковый словарь русского языка">СКАЗКА</a>
                </h3>
                <a href="https://ozhegov.slovaronline.com" title="Толковый словарь русского языка" class="search-link">Толковый словарь русского языка</a>
                <p class="truncate">произведение о вымышленных лицах и событиях</p>
            </div>
        """.trimIndent()
        val successResponse = Response.success(htmlResponse)

        val actual = ozhegovParser.parse(successResponse)
        val expected = WordDefinition.Base(
            word = "СКАЗКА",
            glossary = "Толковый словарь русского языка",
            definition = "произведение о вымышленных лицах и событиях",
            articleUrl = "https://ozhegov.slovaronline.com/31899-SKAZKA"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `error Ozhegov Slovar html response parsing`() {
        val ozhegovParser = HtmlParser.OzhegovSlovarOnlineComParser()
        val errorResponse = Response.error<String>(404, "Not found".toResponseBody())

        val actual = ozhegovParser.parse(errorResponse)
        val expected = WordDefinition.NotFound
        assertEquals(expected, actual)
    }

    @Test
    fun `correct GufoMeParser html response parsing`() {
        val gufoMeParser = HtmlParser.GufoMeParser()
        val htmlResponse = """
         <section id="dictionary-search">
            <ol>
               <li>
                <a href="/dict/bse/%D0%A1%D0%BA%D0%B0%D0%B7%D0%BA%D0%B0">Сказка</a>
                <p>Один из основных жанров устного народно-поэтического творчества</p>
                <span class="small">Большая советская энциклопедия</span>
                <hr>
               </li>
            </ol>
           </section>
        """.trimIndent()
        val successResponse = Response.success(htmlResponse)

        val actual = gufoMeParser.parse(successResponse)
        val expected = WordDefinition.Base(
            word = "Сказка",
            glossary = "Большая советская энциклопедия",
            definition = "Один из основных жанров устного народно-поэтического творчества",
            articleUrl = "https://gufo.me/dict/bse/%D0%A1%D0%BA%D0%B0%D0%B7%D0%BA%D0%B0"
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `error GufoMe html response parsing`() {
        val gufoMeParser = HtmlParser.GufoMeParser()
        val errorResponse = Response.error<String>(404, "Not found".toResponseBody())

        val actual = gufoMeParser.parse(errorResponse)
        val expected = WordDefinition.NotFound
        assertEquals(expected, actual)
    }

    @Test
    fun `correct parsing of several results with priority`() {
        val ozhegovParser = HtmlParser.OzhegovSlovarOnlineComParser()
        val htmlResponse = """
            <div class="search-result highlight">
                <h3>
                    <a href="https://LINK1">Термин1</a>
                </h3>
                <a title="Энциклопедия моды и одежды" class="search-link">Энциклопедия моды и одежды</a>
                <p class="truncate">Определение1</p>
            </div>
            <div class="search-result highlight">
                <h3>
                    <a href="https://LINK2">Термин2</a>
                </h3>
                <a title="Словарь синонимов" class="search-link">Словарь синонимов</a>
                <p class="truncate">Определение2</p>
            </div>
            <div class="search-result highlight">
                <h3>
                    <a href="https://LINK3">Термин3</a>
                </h3>
                <a title="Города России" class="search-link">Города России</a>
                <p class="truncate">Определение3</p>
            </div>
        """.trimIndent()
        val successResponse = Response.success(htmlResponse)

        val actual = ozhegovParser.parse(successResponse)
        val expected = WordDefinition.Base(
            word = "Термин3",
            glossary = "Города России",
            definition = "Определение3",
            articleUrl = "https://LINK3"
        )
        assertEquals(expected, actual)
    }
}
