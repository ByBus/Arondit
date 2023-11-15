package host.capitalquiz.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRuleDao {

    @Insert
    suspend fun insert(gameRule: GameRuleData): Long

    @Query("DELETE FROM game_rules WHERE id=:id")
    suspend fun deleteGameRuleById(id: Long)

    @Query("SELECT * FROM game_rules WHERE id=(SELECT ruleId FROM games WHERE id=:gameId)")
    suspend fun findGameRuleByGameId(gameId: Long): GameRuleData

    @Query("SELECT * FROM game_rules WHERE id=:id")
    suspend fun findGameRuleById(id: Long): GameRuleData

    @Query("SELECT * FROM game_rules")
    fun findAllGameRules(): Flow<List<GameRuleWithGamesData>>
}