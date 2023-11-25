package host.capitalquiz.core.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import host.capitalquiz.core.db.converters.DateTypeConverter
import host.capitalquiz.core.db.converters.GameRuleConverter
import host.capitalquiz.core.db.converters.IntListConverter
import host.capitalquiz.core.db.converters.UserAndColorMapConverter

@Database(
    entities = [GameData::class, PlayerData::class, WordData::class, GameRuleData::class, FieldData::class],
    version = 5,
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 3, to = 4), AutoMigration(
        from = 4,
        to = 5,
        spec = GameDataBase.Migration4To5::class
    )]
)
@TypeConverters(
    DateTypeConverter::class,
    IntListConverter::class,
    UserAndColorMapConverter::class,
    GameRuleConverter::class
)
abstract class GameDataBase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    abstract fun fieldDao(): FieldDao

    abstract fun wordDao(): WordDao

    abstract fun gameRuleDao(): GameRuleDao


    @DeleteColumn.Entries(
        DeleteColumn(tableName = "players", columnName = "color"),
        DeleteColumn(tableName = "players", columnName = "gameId"),
    )
    @RenameColumn.Entries(
        RenameColumn(tableName = "words", fromColumnName = "playerId", toColumnName = "fieldId")
    )
    class Migration4To5 : AutoMigrationSpec

    companion object {

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