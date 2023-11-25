package host.capitalquiz.gameslist.data.mappers

import host.capitalquiz.core.db.FieldData
import host.capitalquiz.core.db.FieldWithPlayerAndWordsData
import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.mappers.FieldDataMapperWithParameter
import host.capitalquiz.core.db.mappers.WordDataMapperWithParameter
import host.capitalquiz.gameslist.domain.PlayerInfo
import javax.inject.Inject

class FieldDataToPlayerInfoMapper @Inject constructor(
    private val mapper: WordDataMapperWithParameter<@JvmSuppressWildcards Map<Char, Int>, Int>,
) : FieldDataMapperWithParameter<GameRuleData, PlayerInfo> {
    private var dictionary = mapOf<Char, Int>()

    override fun map(field: FieldWithPlayerAndWordsData, param: GameRuleData): PlayerInfo {
        dictionary = param.points
        return field.map(this)
    }

    override fun invoke(player: PlayerData, words: List<WordData>, field: FieldData): PlayerInfo {
        val totalScore = words.sumOf { mapper.map(it, dictionary) }
        return PlayerInfo(player.name, field.color, totalScore)
    }
}