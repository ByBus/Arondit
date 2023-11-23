package host.capitalquiz.core.db.mappers

import host.capitalquiz.core.db.WordData

interface WordDataMapper<R> {
    operator fun invoke(
        word: String,
        letterBonuses: List<Int>,
        multiplier: Int,
        id: Long,
        playerId: Long,
        extraPoints: Int,
    ): R
}

interface WordDataMapperWithParameter<P, R> : WordDataMapper<R> {
    fun map(word: WordData, param: P): R
}