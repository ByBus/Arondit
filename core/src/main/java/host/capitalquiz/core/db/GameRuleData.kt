package host.capitalquiz.core.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_rules")
data class GameRuleData(
    val name: String,
    val points: Map<Char, Int>,
    val readOnly: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    fun <R> map(mapper: GameRuleDataMapper<R>): R {
        return mapper(name, points, readOnly, id)
    }
}
