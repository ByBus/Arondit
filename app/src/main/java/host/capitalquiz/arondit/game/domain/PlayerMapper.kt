package host.capitalquiz.arondit.game.domain

interface PlayerMapper<R> {
    operator fun invoke(id: Long, name: String, color: Int, score: Int, words: List<Word>): R
}