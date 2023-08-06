package host.capitalquiz.arondit.game.data

import host.capitalquiz.arondit.core.db.WordDataMapper
import host.capitalquiz.arondit.core.db.PlayerData
import host.capitalquiz.arondit.core.db.PlayerDataMapper
import host.capitalquiz.arondit.core.db.WordData
import host.capitalquiz.arondit.game.domain.Player
import host.capitalquiz.arondit.game.domain.Word
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