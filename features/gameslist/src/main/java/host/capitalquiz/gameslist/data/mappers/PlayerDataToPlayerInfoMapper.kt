package host.capitalquiz.gameslist.data.mappers

import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.PlayerWithWordsData
import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.mappers.PlayerDataMapperWithParameter
import host.capitalquiz.core.db.mappers.WordDataMapperWithParameter
import host.capitalquiz.gameslist.domain.PlayerInfo
import javax.inject.Inject

class PlayerDataToPlayerInfoMapper @Inject constructor(
    private val mapper: WordDataMapperWithParameter<@JvmSuppressWildcards Map<Char, Int>, Int>
) : PlayerDataMapperWithParameter<GameRuleData, PlayerInfo> {
    private var dictionary = mapOf<Char, Int>()

    override fun map(player: PlayerWithWordsData, param: GameRuleData): PlayerInfo {
        dictionary = param.points
        return player.map(this)
    }

    override fun invoke(player: PlayerData, words: List<WordData>): PlayerInfo {
        val totalScore = words.sumOf { mapper.map(it, dictionary) }
        return PlayerInfo(player.name, player.color, totalScore)
    }
}