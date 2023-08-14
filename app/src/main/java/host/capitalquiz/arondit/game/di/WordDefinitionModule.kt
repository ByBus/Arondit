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
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class WordDefinitionModuleBinds {

    @Binds
    abstract fun bindHttpParser(impl: HtmlParser.OzhegovSlovarOnlineComParser): HtmlParser

    @Binds
    abstract fun bindGlossary(impl: Glossary.OzhegovSlovarOnlineCom): Glossary

    @Binds
    abstract fun bindDefinitionRepository(impl: WordDefinitionRepository): DefinitionRepository

    @Binds
    abstract fun bindDefinitionToUiMapper(impl: WordDefinitionToUiMapper): WordDefinitionMapper<WordDefinitionUi>
}