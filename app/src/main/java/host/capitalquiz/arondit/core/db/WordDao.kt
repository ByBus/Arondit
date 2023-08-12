package host.capitalquiz.arondit.core.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: WordData): Long

    @Upsert
    suspend fun insertOrUpdate(word: WordData): Long

    @Query("DELETE FROM words WHERE id=:id")
    suspend fun deleteWordById(id: Long)

    @Query("SELECT * FROM words WHERE id=:id")
    suspend fun selectWordById(id: Long): WordData

    @Update
    suspend fun updateWord(word: WordData)
}