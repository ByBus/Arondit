package host.capitalquiz.editgamerule.domain

interface LetterAddResultMapper<R> {

    fun mapSuccess(letter: Char, points: Int): R

    fun mapError(letter: Char, points: Int): R

}

interface LetterAddResultMapperWithParameter<P, R>: LetterAddResultMapper<R> {
    fun map(result: LetterAddResult, param: P): R
}