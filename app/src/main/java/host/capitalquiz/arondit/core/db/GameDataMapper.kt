package host.capitalquiz.arondit.core.db

interface GameDataMapper<R> {
    operator fun invoke(game: GameData, players: List<PlayerWithWordsData>): R

}