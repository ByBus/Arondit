package host.capitalquiz.arondit.game.data.glossary

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface GlossaryApi {

    @GET
    suspend fun requestWord(@Url url: String): Response<String>
}