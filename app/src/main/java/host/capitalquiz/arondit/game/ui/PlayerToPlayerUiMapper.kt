package host.capitalquiz.arondit.game.ui

import host.capitalquiz.arondit.game.domain.PlayerMapper
import host.capitalquiz.arondit.game.domain.Word
import host.capitalquiz.arondit.game.domain.WordMapper
import javax.inject.Inject

class PlayerToPlayerUiMapper @Inject constructor(
    private val mapper: WordMapper<WordUi>,
) : PlayerMapper<PlayerUi> {
    override fun invoke(
        id: Long,
        name: String,
        color: Int,
        score: Int,
        words: List<Word>,
    ): PlayerUi {
        return PlayerUi(id, color, name, words.map { mapper.invoke(it) }, score)
    }
}