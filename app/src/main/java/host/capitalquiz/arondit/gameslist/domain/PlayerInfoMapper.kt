package host.capitalquiz.arondit.gameslist.domain

interface PlayerInfoMapper<R> {
    operator fun invoke(name: String, color: Int, score: Int): R
}
