package host.capitalquiz.game.domain.mappers

import host.capitalquiz.game.domain.Word

interface WordMapperWithParameter<P, R> : WordMapper<R> {
    fun map(word: Word, param: P): R
}