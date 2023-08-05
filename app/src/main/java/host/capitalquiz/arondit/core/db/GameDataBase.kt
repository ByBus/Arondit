package host.capitalquiz.arondit.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [GameData::class, PlayerData::class, WordData::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class, IntListConverter::class, UserAndColorMapConverter::class)
abstract class GameDataBase: RoomDatabase() {
    abstract fun gameDao(): GameDao

    abstract fun playerDao(): PlayerDao

    abstract fun wordDao(): WordDao

    companion object {
        fun database(context: Context): GameDataBase {
            return Room.inMemoryDatabaseBuilder(
                context,
                GameDataBase::class.java
            ).build()
        }
    }
}