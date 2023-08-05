package host.capitalquiz.arondit.gameslist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GamesListImplModule {

    @Provides
    @Singleton
    fun provideCharToScoreDictionary(): Map<Char, Int> {
        return mapOf(
            'А' to 1, 'Б' to 3, 'В' to 2, 'Г' to 3, 'Д' to 2, 'Е' to 1, 'Ё' to 1, 'Ж' to 5,
            'З' to 5, 'И' to 1, 'Й' to 2, 'К' to 2, 'Л' to 3, 'М' to 2, 'Н' to 1, 'О' to 1,
            'П' to 2, 'Р' to 2, 'С' to 2, 'Т' to 2, 'У' to 3, 'Ф' to 10, 'Х' to 5, 'Ц' to 10,
            'Ч' to 5, 'Ш' to 10, 'Щ' to 10, 'Ъ' to 10, 'Ы' to 5, 'Ь' to 5, 'Э' to 10, 'Ю' to 10,
            'Я' to 3, '*' to 0
        )
    }

}