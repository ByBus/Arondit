package host.capitalquiz.core.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import host.capitalquiz.core.db.mappers.PlayerDataMapper

@Entity(
    tableName = "players",
    foreignKeys = [ForeignKey(
        entity = GameData::class,
        parentColumns = arrayOf("id"),
        childColumns = ["gameId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlayerData(val name: String, val color: Int, val gameId: Long) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}

data class PlayerWithWordsData(
    @Embedded val player: PlayerData,
    @Relation(
        parentColumn = "id",
        entityColumn = "playerId"
    )
    val words: List<WordData>,
) {
    fun <R>map(mapper: PlayerDataMapper<R>): R {
        return mapper(player, words)
    }
}