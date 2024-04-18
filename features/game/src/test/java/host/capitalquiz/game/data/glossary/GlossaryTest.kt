package host.capitalquiz.game.data.glossary

import host.capitalquiz.game.domain.WordDefinition
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class GlossaryTest {

    @Test
    fun `correct Ozhegov glossary api response`() = runTest {
        val ozhegovSlovarGlossary = Glossary.OzhegovSlovarOnlineCom(
            FakeGlossaryApi(returnError = false, throwIoException = false),
            FakeHTMLParser()
        )

        val expected = WordDefinition.Base("word", "glossary", "definition")
        val actual = ozhegovSlovarGlossary.requestDefinition("word")

        assertEquals(expected, actual)
    }

    @Test
    fun `empty result if definition not found`() = runTest {
        val ozhegovSlovarGlossary = Glossary.OzhegovSlovarOnlineCom(
            FakeGlossaryApi(returnError = true, throwIoException = false),
            FakeHTMLParser()
        )

        val expected = WordDefinition.Empty
        val actual = ozhegovSlovarGlossary.requestDefinition("word")

        assertEquals(expected, actual)
    }

    @Test
    fun `return NOT FOUND result if server error`() = runTest {
        val ozhegovSlovarGlossary = Glossary.OzhegovSlovarOnlineCom(
            FakeGlossaryApi(returnError = false, throwIoException = true),
            FakeHTMLParser()
        )

        val expected = WordDefinition.NotFound
        val actual = ozhegovSlovarGlossary.requestDefinition("")

        assertEquals(expected, actual)
    }

    @Test
    fun `get definition from second site if not found on first site`() = runTest {
        val ozhegovSlovarGlossary = Glossary.OzhegovSlovarOnlineCom(
            FakeGlossaryApi(returnError = true, throwIoException = false),
            FakeHTMLParser()
        )
        val gufoMeGlossary = Glossary.GufoMe(
            FakeGlossaryApi(returnError = false, throwIoException = false),
            FakeHTMLParser()
        )

        ozhegovSlovarGlossary.next(gufoMeGlossary)

        val expected = WordDefinition.Base("word", "glossary", "definition")
        val actual = ozhegovSlovarGlossary.requestDefinition("word")

        assertEquals(expected, actual)
    }
}

private class FakeGlossaryApi(var returnError: Boolean, val throwIoException: Boolean) :
    GlossaryApi {
    override suspend fun requestWord(url: String): Response<String> {
        if (throwIoException) throw IOException()
        return if (returnError)
            Response.error(500, "".toResponseBody())
        else
            Response.success("")
    }
}

private class FakeHTMLParser : HtmlParser {
    override fun parse(htmlResult: Response<String>): WordDefinition {
        return if (htmlResult.isSuccessful)
            WordDefinition.Base("word", "glossary", "definition")
        else
            WordDefinition.NotFound
    }
}