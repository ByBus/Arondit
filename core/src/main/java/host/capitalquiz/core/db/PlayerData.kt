package host.capitalquiz.core.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class PlayerData(val name: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}
