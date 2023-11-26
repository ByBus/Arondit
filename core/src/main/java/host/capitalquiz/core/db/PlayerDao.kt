package host.capitalquiz.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerDao {

    @Insert
    suspend fun insert(player: PlayerData): Long

    @Query("SELECT * FROM players")
    suspend fun allPlayers(): List<PlayerData>
}