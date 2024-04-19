package host.capitalquiz.core.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface WordDao {

    @Upsert
    suspend fun insertOrUpdate(word: WordData): Long

    @Query("DELETE FROM words WHERE id=:id")
    suspend fun deleteWordById(id: Long)

    @Query("SELECT * FROM words WHERE id=:id")
    suspend fun selectWordById(id: Long): WordData

    @Query(
        """
        SELECT EXISTS(     
            SELECT word
            FROM words 
                JOIN game_fields ON words.fieldId = game_fields.id                
            WHERE game_fields.gameId = (SELECT gameId FROM game_fields WHERE id = :fieldId) 
                AND words.id != :wordId AND word LIKE :word
        ) AS result
    """
    )
    suspend fun isWordExist(fieldId: Long, word: String, wordId: Long): Boolean
}