package host.capitalquiz.core.db.mappers

import host.capitalquiz.core.db.FieldData
import host.capitalquiz.core.db.FieldWithPlayerAndWordsData
import host.capitalquiz.core.db.PlayerData
import host.capitalquiz.core.db.WordData

interface FieldDataMapper<R> {
    operator fun invoke(player: PlayerData, words: List<WordData>, field: FieldData): R
}

interface FieldDataMapperWithParameter<P, R> : FieldDataMapper<R> {
    fun map(field: FieldWithPlayerAndWordsData, param: P): R
}
