package host.capitalquiz.core.db.converters

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GameRuleConverter {
    @TypeConverter
    fun fromMap(map: Map<Char, Int>): String = Json.encodeToString(map)
    @TypeConverter
    fun toMap(value: String):Map<Char, Int> = Json.decodeFromString(value)
}