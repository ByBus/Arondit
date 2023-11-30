package host.capitalquiz.core.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PlayerDao {

    @Upsert
    suspend fun upsert(player: PlayerData): Long

    @Query("SELECT * FROM players")
    suspend fun allPlayers(): List<PlayerData>

    @Query("DELETE FROM players WHERE id=:id")
    suspend fun deletePlayer(id: Long)
}