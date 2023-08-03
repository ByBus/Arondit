package host.capitalquiz.arondit.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(tableName = "games")
data class GameData(val date: Date) {
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
)