package host.capitalquiz.arondit.core.db

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserAndColorMapConverter {
    @TypeConverter
    fun fromMap(map: Map<String, Int>): String = Json.encodeToString(map)
    @TypeConverter
    fun toMap(value: String):Map<String, Int> = Json.decodeFromString(value)
}