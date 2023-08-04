package host.capitalquiz.arondit.db

interface GameDataMapper<R> {
    operator fun invoke(game: GameData, players: List<PlayerWithWordsData>): R

}