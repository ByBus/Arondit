package host.capitalquiz.core.db

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface GameRuleDao {

    @Insert
    suspend fun insert(gameRule: GameRuleData): Long
}