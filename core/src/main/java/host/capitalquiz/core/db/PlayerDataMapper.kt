package host.capitalquiz.core.db

interface PlayerDataMapper<R> {
    operator fun invoke(player: PlayerData, words: List<WordData>): R
}

interface PlayerDataMapperWithParameter<P, R>: PlayerDataMapper<R> {
    fun map(player: PlayerWithWordsData, param: P): R
}
