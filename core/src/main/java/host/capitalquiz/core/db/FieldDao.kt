package host.capitalquiz.core.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldDao {
    @Insert
    suspend fun insert(filed: FieldData): Long

    @Transaction
    @Query("SELECT * FROM game_fields WHERE id=:fieldId")
    fun playerWithWords(fieldId: Long): LiveData<FieldWithPlayerAndWordsData>

    @Transaction
    @Query("SELECT * FROM game_fields WHERE gameId=:gameId")
    fun allFieldsOfGame(gameId: Long): Flow<List<FieldWithPlayerAndWordsData>>

    @Query("DELETE FROM game_fields WHERE id=:filedId")
    suspend fun deleteFieldById(filedId: Long)
}