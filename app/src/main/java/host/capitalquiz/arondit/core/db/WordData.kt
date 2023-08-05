package host.capitalquiz.arondit.core.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    fun <R> map(mapper: WordDataMapper<R>): R {
        return mapper(word, letterBonuses, multiplier, id)
    }
}
