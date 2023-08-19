package host.capitalquiz.arondit.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import host.capitalquiz.arondit.core.db.GameDao
import host.capitalquiz.arondit.core.db.GameDataBase
import host.capitalquiz.arondit.core.db.PlayerDao
import host.capitalquiz.arondit.core.db.WordDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext context: Context
    ): GameDataBase = Room.inMemoryDatabaseBuilder(
        context,
        GameDataBase::class.java,
    ).build()

//    @Singleton
//    @Provides
//    fun provideYourDatabase(
//        @ApplicationContext context: Context
//    ): GameDataBase = Room.databaseBuilder(
//        context,
//        GameDataBase::class.java,
//        "games.db"
//    ).build()

    @Provides
    fun provideGameDao(db: GameDataBase): GameDao = db.gameDao()

    @Provides
    fun providePlayerDao(db: GameDataBase): PlayerDao = db.playerDao()

    @Provides
    fun provideWordDao(db: GameDataBase): WordDao = db.wordDao()
}