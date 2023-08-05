package host.capitalquiz.arondit.core.db

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class IntListConverter {

    @TypeConverter
    fun fromList(list: List<Int>): String = Json.encodeToString(list)

    @TypeConverter
    fun toList(value: String): List<Int> = Json.decodeFromString(value)
}