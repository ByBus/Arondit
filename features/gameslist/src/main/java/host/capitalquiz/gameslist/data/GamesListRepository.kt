package host.capitalquiz.gameslist.data

import host.capitalquiz.core.db.GameDao
import host.capitalquiz.core.db.GameData
import host.capitalquiz.core.db.mappers.GameDataMapper
import host.capitalquiz.gameslist.domain.Game
import host.capitalquiz.gameslist.domain.GamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class GamesListRepository @Inject constructor(
    private val gameDao: GameDao,
    private val mapper: GameDataMapper<Game>,
) : GamesRepository {
    override fun allGames(): Flow<List<Game>> {
        return gameDao.allGames().map { games ->
            games.map {
                mapper.invoke(it.game, it.players, it.gameRule)
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