package host.capitalquiz.core.db

interface PlayerDataMapper<R> {
    operator fun invoke(player: PlayerData, words: List<WordData>): R

}