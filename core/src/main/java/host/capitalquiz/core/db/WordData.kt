package host.capitalquiz.core.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import host.capitalquiz.core.db.mappers.WordDataMapper

@Entity(
    tableName = "words",
    foreignKeys = [ForeignKey(
        entity = PlayerData::class,
        parentColumns = arrayOf("id"),
        childColumns = ["playerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class WordData(
    val word: String,
    val letterBonuses: List<Int> = emptyList(),
    val multiplier: Int = 1,
    val playerId: Long,
    @ColumnInfo(name = "additional_bonus")
    val extraPoints: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
    fun <R> map(mapper: WordDataMapper<R>): R {
        return mapper(word, letterBonuses, multiplier, id, playerId, extraPoints)
    }

    fun deepCopy(
        word: String = this.word,
        letterBonuses: List<Int> = this.letterBonuses,
        multiplier: Int = this.multiplier,
        playerId: Long = this.playerId,
        extraPoints: Int = this.extraPoints
    ): WordData {
        return copy(
            word = word,
            letterBonuses = letterBonuses,
            multiplier = multiplier,
            playerId = playerId,
            extraPoints = extraPoints
        ).also {
            it.id = id
        }
    }
}
