package host.capitalquiz.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldDao {
    @Insert
    suspend fun insert(field: FieldData): Long

    @Transaction
    @Query("SELECT * FROM game_fields WHERE gameId=:gameId")
    fun allFieldsOfGame(gameId: Long): Flow<List<FieldWithPlayerAndWordsData>>

    @Query("DELETE FROM game_fields WHERE id=:filedId")
    suspend fun deleteFieldById(filedId: Long)

    @Transaction
    @Query("SELECT * from game_fields WHERE id=:fieldId")
    suspend fun findFieldById(fieldId: Long): FieldWithPlayerAndWordsData

    @Query("SELECT id FROM game_fields WHERE playerId=:playerId")
    suspend fun findFieldsIdsWithPlayer(playerId: Long): List<Long>

    @Query("SELECT id FROM game_fields WHERE gameId=:gameId")
    suspend fun findAllFieldsIdsOfGame(gameId: Long): List<Long>
}