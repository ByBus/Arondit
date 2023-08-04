package host.capitalquiz.arondit.db

interface PlayerDataMapper<R> {
    operator fun invoke(player: PlayerData, words: List<WordData>): R

}