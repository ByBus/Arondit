package host.capitalquiz.core.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface GameDao {

    @Insert
    suspend fun insert(gameData: GameData): Long

    @Query("UPDATE games SET ruleId=:ruleId WHERE id = :gameId")
    suspend fun updateGameRule(gameId: Long, ruleId: Long)

    @Transaction
    @Query("SELECT * FROM games")
    fun allGames(): LiveData<List<GameWithPlayersData>>

    @Query("DELETE FROM games WHERE id=:id")
    suspend fun deleteGameById(id: Long)
}