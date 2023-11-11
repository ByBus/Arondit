package host.capitalquiz.core.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(tableName = "games")
data class GameData(val date: Date, val ruleId: Long = 1L) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

}

data class GameWithPlayersData(
    @Embedded val game: GameData,
    @Relation(
        entity = PlayerData::class,
        parentColumn = "id",
        entityColumn = "gameId"
    )
    val players: List<PlayerWithWordsData>,
    @Relation(
        parentColumn = "ruleId",
        entityColumn = "id"
    )
    val gameRule: GameRuleData
)  {
    fun <R>map(mapper: GameDataMapper<R>): R {
        return mapper(game, players, gameRule)
    }
}