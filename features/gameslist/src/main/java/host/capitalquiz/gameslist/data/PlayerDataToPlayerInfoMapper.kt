package host.capitalquiz.gameslist.data

import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.PlayerDataMapper
import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.WordDataMapper
import host.capitalquiz.gameslist.domain.PlayerInfo
import javax.inject.Inject

class PlayerDataToPlayerInfoMapper @Inject constructor(
    private val mapper: WordDataMapper<Int>,
) : PlayerDataMapper<PlayerInfo> {
    override fun invoke(player: PlayerData, words: List<WordData>): PlayerInfo {
        val totalScore = words.sumOf { it.map(mapper) }
        return PlayerInfo(player.name, player.color, totalScore)
    }
}