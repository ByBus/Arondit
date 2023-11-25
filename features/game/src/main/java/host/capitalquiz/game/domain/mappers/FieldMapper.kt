package host.capitalquiz.game.domain.mappers

import host.capitalquiz.game.domain.Field
import host.capitalquiz.game.domain.Word

interface FieldMapper<R> {
    operator fun invoke(id: Long, name: String, color: Int, score: Int, words: List<Word>): R
}


interface FieldMapperWithParameter<P, R> : FieldMapper<R> {
    fun map(field: Field, param: P): R
}