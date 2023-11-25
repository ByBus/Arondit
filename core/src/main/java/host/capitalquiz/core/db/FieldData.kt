package host.capitalquiz.core.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import host.capitalquiz.core.db.mappers.FieldDataMapper

@Entity(
    tableName = "game_fields",
    foreignKeys = [ForeignKey(
        entity = GameData::class,
        parentColumns = arrayOf("id"),
        childColumns = ["gameId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class FieldData(
    val color: Int,
    val gameId: Long,
    val playerId: Long,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}

data class FieldWithPlayerAndWordsData(
    @Embedded val field: FieldData,
    @Relation(
        parentColumn = "playerId",
        entityColumn = "id"
    )
    val player: PlayerData,
    @Relation(
        parentColumn = "id",
        entityColumn = "fieldId"
    )
    val words: List<WordData>,
) {
    fun <R> map(mapper: FieldDataMapper<R>): R {
        return mapper(player, words, field)
    }
}
