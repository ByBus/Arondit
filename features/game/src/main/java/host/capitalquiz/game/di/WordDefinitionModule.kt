package host.capitalquiz.game.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import host.capitalquiz.game.data.glossary.Glossary
import host.capitalquiz.game.data.glossary.GlossaryApi
import host.capitalquiz.game.data.glossary.HtmlParser
import host.capitalquiz.game.data.glossary.WordDefinitionRepository
import host.capitalquiz.game.domain.DefinitionRepository
import host.capitalquiz.game.domain.mappers.WordDefinitionMapper
import host.capitalquiz.game.ui.dialog.WordDefinitionUi
import host.capitalquiz.game.ui.dialog.mappers.WordDefinitionToUiMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

@Module
@InstallIn(ViewModelComponent::class)
interface WordDefinitionModule {

    @Binds
    @OzhegovGlossary
    fun bindOzhegovHttpParser(impl: HtmlParser.OzhegovSlovarOnlineComParser): HtmlParser

    @Binds
    @GufoMeGlossary
    fun bindGufoMeHttpParser(impl: HtmlParser.GufoMeParser): HtmlParser

    @Binds
    @OzhegovGlossary
    fun bindOzhegovGlossary(impl: Glossary.OzhegovSlovarOnlineCom): Glossary

    @Binds
    @GufoMeGlossary
    fun bindGufoMeGlossary(impl: Glossary.GufoMe): Glossary

    @Binds
    fun bindDefinitionRepository(impl: WordDefinitionRepository): DefinitionRepository

    @Binds
    fun bindDefinitionToUiMapper(impl: WordDefinitionToUiMapper): WordDefinitionMapper<WordDefinitionUi>

    companion object {

        @Provides
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient().newBuilder().build()

        @Provides
        @ViewModelScoped
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://www.google.com")
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }

        @Provides
        @ViewModelScoped
        fun provideGlossaryApi(retrofit: Retrofit): GlossaryApi = retrofit.create()

        @Provides
        @ViewModelScoped
        @CompositeGlossary
        fun provideCompositeGlossary(
            @OzhegovGlossary glossary1: Glossary,
            @GufoMeGlossary glossary2: Glossary,
        ): Glossary {
            glossary1.next(glossary2)
            return glossary1
        }
    }
}