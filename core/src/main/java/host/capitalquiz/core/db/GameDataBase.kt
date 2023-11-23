package host.capitalquiz.core.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import host.capitalquiz.core.db.mappers.DateTypeConverter
import host.capitalquiz.core.db.mappers.GameRuleConverter
import host.capitalquiz.core.db.mappers.IntListConverter
import host.capitalquiz.core.db.mappers.UserAndColorMapConverter

@Database(
    entities = [GameData::class, PlayerData::class, WordData::class, GameRuleData::class],
    version = 4,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 3, to = 4)]
)
@TypeConverters(
    DateTypeConverter::class,
    IntListConverter::class,
    UserAndColorMapConverter::class,
    GameRuleConverter::class
)
abstract class GameDataBase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    abstract fun playerDao(): PlayerDao

    abstract fun wordDao(): WordDao

    abstract fun gameRuleDao(): GameRuleDao

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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE games ADD COLUMN ruleId INTEGER DEFAULT 0 NOT NULL")
            }
        }
    }
}