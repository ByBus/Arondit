package host.capitalquiz.core.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PlayerDao {

    @Insert
    suspend fun insert(player: PlayerData): Long

    @Transaction
    @Query("SELECT * FROM players WHERE id=:id")
    fun playerWithWords(id: Long): LiveData<PlayerWithWordsData>

    @Transaction
    @Query("SELECT * FROM players WHERE gameId=:id")
    fun allPlayerByGameId(id: Long): LiveData<List<PlayerWithWordsData>>

    @Query("DELETE FROM players WHERE id=:id")
    suspend fun deletePlayerById(id: Long)

}