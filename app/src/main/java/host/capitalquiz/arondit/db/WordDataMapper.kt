package host.capitalquiz.arondit.db

interface WordDataMapper<R> {
    operator fun invoke(word: String, letterBonuses: List<Int>, multiplier: Int, id: Long): R

}