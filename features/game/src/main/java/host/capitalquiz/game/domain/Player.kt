package host.capitalquiz.game.domain

import host.capitalquiz.game.domain.mappers.PlayerMapper

data class Player(
    val id: Long = -1,
    val name: String,
    val color: Int,
    val score: Int = 0,
    val words: List<Word> = emptyList(),
) {
    fun <R> map(mapper: PlayerMapper<R>): R {
        return mapper(id, name, color, score, words)
    }
}