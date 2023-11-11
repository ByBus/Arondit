package host.capitalquiz.game.ui

import host.capitalquiz.game.domain.GameRuleSimple
import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.PlayerMapper
import host.capitalquiz.game.domain.PlayerMapperWithParameter
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.WordMapper
import host.capitalquiz.game.domain.WordMapperWithParameter
import javax.inject.Inject

class PlayerToPlayerUiMapper @Inject constructor(
    private val wordMapper: WordMapperWithParameter<GameRuleSimple, WordUi>,
) : PlayerMapperWithParameter<GameRuleSimple, PlayerUi> {
    private var rule: GameRuleSimple = GameRuleSimple(emptyMap())

    override fun map(player: Player, param: GameRuleSimple): PlayerUi {
        rule = param
        return player.map(this)
    }

    override fun invoke(
        id: Long,
        name: String,
        color: Int,
        score: Int,
        words: List<Word>,
    ): PlayerUi {
        return PlayerUi(id, color, name, words.map { wordMapper.map(it, rule) }, score)
    }
}