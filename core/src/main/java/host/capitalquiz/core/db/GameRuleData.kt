package host.capitalquiz.core.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import host.capitalquiz.core.db.mappers.GameRuleDataMapper
import host.capitalquiz.core.db.mappers.GameRuleWithGamesMapper

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


data class GameRuleWithGamesData(
    @Embedded val gameRule: GameRuleData,
    @Relation(
        parentColumn = "id",
        entityColumn = "ruleId"
    )
    val games: List<GameData>
) {
    fun <R> map(mapper: GameRuleWithGamesMapper<R>): R {
        return mapper(gameRule.id, gameRule.name, gameRule.points, gameRule.readOnly, games)
    }
}
