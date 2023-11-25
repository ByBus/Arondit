package host.capitalquiz.core.db.mappers

import host.capitalquiz.core.db.FieldWithPlayerAndWordsData
import host.capitalquiz.core.db.GameData
import host.capitalquiz.core.db.GameRuleData

interface GameDataMapper<R> {
    operator fun invoke(
        game: GameData,
        players: List<FieldWithPlayerAndWordsData>,
        gameRule: GameRuleData,
    ): R
}
//
//interface GameDataMapperWithParameter<P, R> : GameDataMapper<R> {
//    fun map(game: GameData, param: R) : R
//}