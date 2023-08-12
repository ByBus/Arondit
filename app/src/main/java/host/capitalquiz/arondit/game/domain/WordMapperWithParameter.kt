package host.capitalquiz.arondit.game.domain

interface WordMapperWithParameter<T, I, R> : WordMapper<R>{
    fun map(word: I, param: T): R
}