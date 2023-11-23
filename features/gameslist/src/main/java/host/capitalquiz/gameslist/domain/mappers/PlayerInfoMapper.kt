package host.capitalquiz.gameslist.domain.mappers

interface PlayerInfoMapper<R> {
    operator fun invoke(name: String, color: Int, score: Int): R
}
