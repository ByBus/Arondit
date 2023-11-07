package host.capitalquiz.game.domain

data class Player(val id: Long = -1, val name: String, val color: Int, val score: Int = 0, val words: List<Word> = emptyList()) {
    fun <R> map(mapper: PlayerMapper<R>): R {
        return mapper(id, name, color, score, words)
    }
}