package host.capitalquiz.arondit.game.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import host.capitalquiz.arondit.game.data.glossary.Glossary
import host.capitalquiz.arondit.game.data.glossary.GlossaryApi
import host.capitalquiz.arondit.game.data.glossary.HtmlParser
import host.capitalquiz.arondit.game.data.glossary.WordDefinitionRepository
import host.capitalquiz.arondit.game.domain.DefinitionRepository
import host.capitalquiz.arondit.game.domain.WordDefinitionMapper
import host.capitalquiz.arondit.game.ui.dialog.WordDefinitionToUiMapper
import host.capitalquiz.arondit.game.ui.dialog.WordDefinitionUi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

@Module
@InstallIn(ViewModelComponent::class)
class WordDefinitionModule {

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

@Module
@InstallIn(ViewModelComponent::class)
abstract class WordDefinitionModuleBinds {

    @Binds
    @OzhegovGlossary
    abstract fun bindOzhegovHttpParser(impl: HtmlParser.OzhegovSlovarOnlineComParser): HtmlParser

    @Binds
    @GufoMeGlossary
    abstract fun bindGufoMeHttpParser(impl: HtmlParser.GufoMeParser): HtmlParser

    @Binds
    @OzhegovGlossary
    abstract fun bindOzhegovGlossary(impl: Glossary.OzhegovSlovarOnlineCom): Glossary

    @Binds
    @GufoMeGlossary
    abstract fun bindGufoMeGlossary(impl: Glossary.GufoMe): Glossary

    @Binds
    abstract fun bindDefinitionRepository(impl: WordDefinitionRepository): DefinitionRepository

    @Binds
    abstract fun bindDefinitionToUiMapper(impl: WordDefinitionToUiMapper): WordDefinitionMapper<WordDefinitionUi>
}