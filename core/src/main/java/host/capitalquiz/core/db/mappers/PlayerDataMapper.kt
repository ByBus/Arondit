package host.capitalquiz.core.db.mappers

import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.PlayerWithWordsData
import host.capitalquiz.core.db.WordData

interface PlayerDataMapper<R> {
    operator fun invoke(player: PlayerData, words: List<WordData>): R
}

interface PlayerDataMapperWithParameter<P, R> : PlayerDataMapper<R> {
    fun map(player: PlayerWithWordsData, param: P): R
}
