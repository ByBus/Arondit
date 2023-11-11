package host.capitalquiz.core.db

interface GameDataMapper<R> {
    operator fun invoke(game: GameData, players: List<PlayerWithWordsData>, gameRule: GameRuleData): R
}
//
//interface GameDataMapperWithParameter<P, R> : GameDataMapper<R> {
//    fun map(game: GameData, param: R) : R
//}