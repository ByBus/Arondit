package host.capitalquiz.editgamerule.domain

sealed interface LetterResult {
    val ruleId: Long
    fun letter(): Char
    fun points(): Int
    fun <R> map(mapper: LetterAddResultMapper<R>): R

    class Success(private val nextLetter: Char, override val ruleId: Long) : LetterResult {
        override fun letter(): Char = nextLetter
        override fun points(): Int = 1
        override fun <R> map(mapper: LetterAddResultMapper<R>): R {
            return mapper.mapSuccess(letter(), points())
        }
    }

    class AlreadyExist(
        private val letter: Char,
        private val points: Int,
        override val ruleId: Long,
        private val oldPoints: Int,
    ) : LetterResult {
        override fun letter(): Char = letter
        override fun points(): Int = points
        override fun <R> map(mapper: LetterAddResultMapper<R>): R {
            return mapper.mapError(letter(), points(), oldPoints)
        }
    }
}