package host.capitalquiz.game.domain

import host.capitalquiz.game.domain.mappers.FieldMapper

data class Field(
    val id: Long = -1,
    val name: String,
    val color: Int,
    val score: Int = 0,
    val words: List<Word> = emptyList(),
    val playerId: Long = -1L,
) {
    fun <R> map(mapper: FieldMapper<R>): R {
        return mapper(id, name, color, score, words)
    }

    fun hasPlayerId(): Boolean = playerId != -1L
}