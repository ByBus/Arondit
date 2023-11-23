package host.capitalquiz.game.domain.mappers

import host.capitalquiz.game.domain.Player
import host.capitalquiz.game.domain.Word

interface PlayerMapper<R> {
    operator fun invoke(id: Long, name: String, color: Int, score: Int, words: List<Word>): R
}


interface PlayerMapperWithParameter<P, R> : PlayerMapper<R> {
    fun map(player: Player, param: P): R
}