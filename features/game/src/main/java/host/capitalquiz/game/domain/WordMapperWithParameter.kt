package host.capitalquiz.game.domain

interface WordMapperWithParameter<P, R> : WordMapper<R>{
    fun map(word: Word, param: P): R
}