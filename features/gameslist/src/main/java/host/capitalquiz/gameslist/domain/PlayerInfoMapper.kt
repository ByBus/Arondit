package host.capitalquiz.gameslist.domain

interface PlayerInfoMapper<R> {
    operator fun invoke(name: String, color: Int, score: Int): R
}
