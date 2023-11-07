package host.capitalquiz.game.ui

import host.capitalquiz.game.domain.PlayerMapper
import host.capitalquiz.game.domain.Word
import host.capitalquiz.game.domain.WordMapper
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
        return PlayerUi(id, color, name, words.map { mapper.map(it) }, score)
    }
}