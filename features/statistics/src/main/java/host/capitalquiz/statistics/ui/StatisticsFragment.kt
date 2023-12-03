package host.capitalquiz.statistics.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import host.capitalquiz.core.ui.BindingFragment
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.core.ui.collect
import host.capitalquiz.statistics.databinding.FragmentStatisticsBinding as StatisticsBinding

class StatisticsFragment : BindingFragment<StatisticsBinding>() {

    private val viewModel: StatisticsViewModel by viewModels()
    override val viewInflater: Inflater<StatisticsBinding> = StatisticsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userNameAdapter = UserNameAdapter()
        binding.columnPlayersNames.apply {
            adapter = userNameAdapter
            setHasFixedSize(true)
        }

        val userStatsAdapter = UserStatsAdapter()
        binding.statisticsRows.apply {
            adapter = userStatsAdapter
            setHasFixedSize(true)
        }

        viewModel.sortedRows.collect(viewLifecycleOwner) { items ->
            userNameAdapter.submitList(items.map { it.playerName })
            userStatsAdapter.submitList(items)
        }

        with(binding.headersRow) {
            totalGames.setOnClickListener { viewModel.sortByGames() }
            victories.setOnClickListener { viewModel.sortByVictories() }
            allGamesScore.setOnClickListener { viewModel.sortByAllGamesScore() }
            victoriesPercent.setOnClickListener { viewModel.sortByVictoriesPercent() }
            wordsTotal.setOnClickListener { viewModel.sortByWordsTotal() }
            wordsPerGame.setOnClickListener { viewModel.sortByWordsPerGame() }
            maxWordsInGame.setOnClickListener { viewModel.sortByMaxWordsInGame() }
            scorePerGame.setOnClickListener { viewModel.sortByScorePerGame() }
            maxScoreInGame.setOnClickListener { viewModel.sortByMaxScoreInGame() }
            allGamesScore.setOnClickListener { viewModel.sortByAllGamesScore() }
            longestWord.setOnClickListener { viewModel.sortByLongestWord() }
            mostValuableWord.setOnClickListener { viewModel.sortByMostValuableWord() }
        }

        TableScrollCoordinator(
            binding.headersHorizontalScroller,
            binding.statisticsHorizontalScroller,
            binding.columnPlayersNames,
            binding.statisticsRows
        )

        userNameAdapter.registerAdapterDataObserver(
            ScrollToPositionObserver(
                binding.columnPlayersNames,
                0
            )
        )
        userStatsAdapter.registerAdapterDataObserver(
            ScrollToPositionObserver(
                binding.statisticsRows,
                0
            )
        )
    }
}