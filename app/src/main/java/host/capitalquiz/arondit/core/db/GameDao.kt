package host.capitalquiz.arondit.core.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface GameDao {

    @Insert
    suspend fun insert(gameData: GameData): Long

    @Transaction
    @Query("SELECT * FROM games")
    fun allGames(): LiveData<List<GameWithPlayersData>>

    @Query("DELETE FROM games WHERE id=:id")
    suspend fun deleteGameById(id: Long)
}