package host.capitalquiz.arondit.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface GamesDao {

    @Insert
    suspend fun insert(gameData: GameData): Long

    @Insert
    suspend fun insert(player: PlayerData): Long

    @Insert
    suspend fun insert(word: WordData): Long

    @Transaction
    @Query("SELECT * FROM games")
    fun allGames(): LiveData<List<GameWithPlayersData>>

    @Transaction
    @Query("SELECT * FROM players WHERE id=:id")
    suspend fun playerWithWords(id: Long): List<PlayerWithWordsData>

    @Query("DELETE FROM games WHERE id=:id")
    suspend fun deleteGameById(id: Long)

    @Query("DELETE FROM players WHERE id=:id")
    suspend fun deletePlayerById(id: Long)

    @Query("DELETE FROM words WHERE id=:id")
    suspend fun deleteWordById(id: Long)

}