package host.capitalquiz.core.db.mappers

import host.capitalquiz.core.db.GameData
import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.PlayerWithWordsData

interface GameDataMapper<R> {
    operator fun invoke(
        game: GameData,
        players: List<PlayerWithWordsData>,
        gameRule: GameRuleData,
    ): R
}
//
//interface GameDataMapperWithParameter<P, R> : GameDataMapper<R> {
//    fun map(game: GameData, param: R) : R
//}