package host.capitalquiz.game.data

import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.PlayerDataMapper
import host.capitalquiz.core.db.WordData
import host.capitalquiz.core.db.WordDataMapper
import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.Word
import javax.inject.Inject


class PlayerDataMapperToPlayer @Inject constructor(
    private val mapper: WordDataMapper<Word>,
    private val dictionary: Map<Char, Int>,
) : PlayerDataMapper<Player> {
    override fun invoke(player: PlayerData, words: List<WordData>): Player {
        val newWords = words.map { it.map(mapper) }
        return Player(
            player.id,
            player.name,
            player.color,
            newWords.sumOf { it.score(dictionary) },
            newWords
        )
    }
}