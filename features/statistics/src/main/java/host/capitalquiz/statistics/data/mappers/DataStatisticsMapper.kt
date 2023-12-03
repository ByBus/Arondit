package host.capitalquiz.statistics.data.mappers

import host.capitalquiz.core.db.FieldWithPlayerAndWordsData
import host.capitalquiz.core.db.GameData
import host.capitalquiz.core.db.GameRuleData
import host.capitalquiz.core.db.mappers.GameDataMapper
import host.capitalquiz.core.db.mappers.WordDataMapperWithParameter
import host.capitalquiz.statistics.domain.UserGameShortStats
import java.lang.Integer.max
import javax.inject.Inject

interface UserStatisticsProducer : GameDataMapper<List<UserGameShortStats>>

class GameDataToUserGameShortStatsMapper @Inject constructor(
    private val wordDataScoreMapper: WordDataMapperWithParameter<@JvmSuppressWildcards Map<Char, Int>, Int>,
) : UserStatisticsProducer {
    private val lengthComparator = Comparator<String> { s1, s2 -> s1.length - s2.length }

    override fun invoke(
        game: GameData,
        players: List<FieldWithPlayerAndWordsData>,
        gameRule: GameRuleData,
    ): List<UserGameShortStats> {
        val result = mutableListOf<UserGameShortStats>()
        var maxScore = 0

        players.forEach { playerWithWords ->
            val longestWord = playerWithWords.words.maxOfWith(lengthComparator) { it.word }

            val wordsToScore = playerWithWords.words
                .associateBy(
                    { it.word },
                    { wordDataScoreMapper.map(it, gameRule.points) }
                )

            val mostValuableWord = wordsToScore.maxBy { it.value }

            val score = wordsToScore.values.sum()
            maxScore = max(score, maxScore)

            result.add(
                UserGameShortStats(
                    playerId = playerWithWords.player.id,
                    playerName = playerWithWords.player.name,
                    longestWord = longestWord,
                    mostValuableWord = mostValuableWord.key,
                    mostValuableWordScore = mostValuableWord.value,
                    wordsCount = wordsToScore.size,
                    score = score,
                    false
                )
            )
        }

        val playersWithHighPoint = result.filter { it.score == maxScore }
        if (playersWithHighPoint.size == 1) {
            val winner = playersWithHighPoint[0]
            result.remove(winner)
            result.add(winner.copy(victory = true))
        }
        return result
    }
}