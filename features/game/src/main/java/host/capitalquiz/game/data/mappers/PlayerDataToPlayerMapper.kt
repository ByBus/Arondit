package host.capitalquiz.game.data.mappers

import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.PlayerWithWordsData
import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.mappers.PlayerDataMapperWithParameter
import host.capitalquiz.core.db.mappers.WordDataMapper
import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.Word
import javax.inject.Inject


class PlayerDataToPlayerMapper @Inject constructor(
    private val wordMapper: WordDataMapper<Word>,
) : PlayerDataMapperWithParameter<GameRuleSimple, Player> {
    private var dictionary: Map<Char, Int> = emptyMap()

    override fun map(player: PlayerWithWordsData, param: GameRuleSimple): Player {
        dictionary = param.dictionary
        return player.map(this)
    }

    override fun invoke(player: PlayerData, words: List<WordData>): Player {
        val newWords = words.map { it.map(wordMapper) }
        return Player(
            player.id,
            player.name,
            player.color,
            newWords.sumOf { it.score(dictionary) },
            newWords
        )
    }
}