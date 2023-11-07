package host.capitalquiz.arondit.gameslist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import host.capitalquiz.core.db.GameDataMapper
import host.capitalquiz.core.db.GameDao
import host.capitalquiz.core.db.GameData
import host.capitalquiz.arondit.gameslist.domain.Game
import host.capitalquiz.arondit.gameslist.domain.GamesRepository
import java.util.Date
import javax.inject.Inject

class GamesListRepository @Inject constructor(
    private val gameDao: GameDao,
    private val mapper: GameDataMapper<Game>,
) : GamesRepository {
    override fun allGames(): LiveData<List<Game>> {
        return gameDao.allGames().map { games ->
            games.map {
                mapper.invoke(it.game, it.players)
            }
        }
    }

    override suspend fun createGame(): Long {
        return gameDao.insert(GameData(Date()))
    }

    override suspend fun deleteGame(id: Long) {
        gameDao.deleteGameById(id)
    }
}