package host.capitalquiz.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [GameData::class, PlayerData::class, WordData::class], version = 2, exportSchema = false)
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

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE words ADD COLUMN additional_bonus INTEGER DEFAULT 0 NOT NULL")
            }
        }
    }
}