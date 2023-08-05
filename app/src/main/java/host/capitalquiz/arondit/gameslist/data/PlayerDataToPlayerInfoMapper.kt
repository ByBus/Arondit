package host.capitalquiz.arondit.gameslist.data

import host.capitalquiz.arondit.core.db.PlayerData
import host.capitalquiz.arondit.core.db.PlayerDataMapper
import host.capitalquiz.arondit.core.db.WordData
import host.capitalquiz.arondit.core.db.WordDataMapper
import host.capitalquiz.arondit.gameslist.domain.PlayerInfo
import javax.inject.Inject

class PlayerDataToPlayerInfoMapper @Inject constructor(
    private val mapper: WordDataMapper<Int>,
) : PlayerDataMapper<PlayerInfo> {
    override fun invoke(player: PlayerData, words: List<WordData>): PlayerInfo {
        val totalScore = words.sumOf { it.map(mapper) }
        return PlayerInfo(player.name, player.color, totalScore)
    }
}