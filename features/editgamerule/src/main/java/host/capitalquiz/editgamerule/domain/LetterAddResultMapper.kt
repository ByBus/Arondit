package host.capitalquiz.editgamerule.domain

interface LetterAddResultMapper<R> {

    fun mapSuccess(letter: Char, points: Int): R

    fun mapError(letter: Char, points: Int, oldPoints: Int): R
}

interface LetterAddResultMapperWithParameter<P, R>: LetterAddResultMapper<R> {
    fun map(result: LetterResult, param: P): R
}