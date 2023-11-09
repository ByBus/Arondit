package host.capitalquiz.gameslist.domain

class PlayerInfo(val name: String, val color: Int, val score: Int) {
    fun <R> map(mapper: PlayerInfoMapper<R>): R{
        return mapper(name, color, score)
    }
}