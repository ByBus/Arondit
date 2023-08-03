package host.capitalquiz.arondit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [GameData::class, PlayerData::class, WordData::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class, IntListConverter::class, UserAndColorMapConverter::class)
abstract class GameDataBase: RoomDatabase() {
    abstract fun gameDao(): GamesDao

    companion object {
        fun database(context: Context): GameDataBase {
            return Room.inMemoryDatabaseBuilder(
                context,
                GameDataBase::class.java
            ).build()
        }
    }
}