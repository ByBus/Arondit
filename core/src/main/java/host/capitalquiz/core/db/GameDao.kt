package host.capitalquiz.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert
    suspend fun insert(gameData: GameData): Long

    @Query("UPDATE games SET ruleId=:ruleId WHERE id = :gameId")
    suspend fun updateGameRule(gameId: Long, ruleId: Long)

    @Transaction
    @Query("SELECT * FROM games")
    fun allGames(): Flow<List<GameWithPlayersData>>

    @Query("DELETE FROM games WHERE id=:id")
    suspend fun deleteGameById(id: Long)

    @Query("SELECT * FROM games WHERE id=:id")
    suspend fun findGameById(id: Long): GameData

    @Query("SELECT * FROM games WHERE ruleId=:ruleId")
    suspend fun findAllGamesWithRule(ruleId: Long): List<GameData>
}