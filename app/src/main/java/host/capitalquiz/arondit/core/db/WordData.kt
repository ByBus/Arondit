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
        return mapper(word, letterBonuses, multiplier, id, playerId)
    }

    fun deepCopy(
        word: String = this.word,
        letterBonuses: List<Int> = this.letterBonuses,
        multiplier: Int = this.multiplier,
        playerId: Long = this.playerId,
    ): WordData {
        return copy(
            word = word,
            letterBonuses = letterBonuses,
            multiplier = multiplier,
            playerId = playerId
        ).also {
            it.id = id
        }
    }
}
