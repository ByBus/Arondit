package host.capitalquiz.game.domain

import host.capitalquiz.game.domain.mappers.FieldMapper

data class Field(
    val id: Long = -1,
    val name: String,
    val color: Int,
    val score: Int = 0,
    val words: List<Word> = emptyList(),
) {
    fun <R> map(mapper: FieldMapper<R>): R {
        return mapper(id, name, color, score, words)
    }
}