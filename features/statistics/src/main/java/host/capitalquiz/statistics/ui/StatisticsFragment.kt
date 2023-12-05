package host.capitalquiz.statistics.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.core.ui.BindingFragment
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.core.ui.collect
import host.capitalquiz.statistics.R
import host.capitalquiz.statistics.databinding.FragmentStatisticsBinding as StatisticsBinding

@AndroidEntryPoint
class StatisticsFragment : BindingFragment<StatisticsBinding>() {

    private val viewModel: StatisticsViewModel by viewModels()
    override val viewInflater: Inflater<StatisticsBinding> = StatisticsBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadStatistics()

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
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

        binding.information.infoImage.setImageResource(R.drawable.joker)
        binding.information.infoText.setText(R.string.no_statistics_yet)
        binding.information.infoButton.setText(R.string.close)

        viewModel.sortedRows.collect(viewLifecycleOwner) { items ->
            userNameAdapter.submitList(items.map { it.playerName })
            userStatsAdapter.submitList(items)
        }

        viewModel.headersState.collect(viewLifecycleOwner) { state ->
            state.update(binding.headersRow.columnNames)
        }

        viewModel.showInformation.collect(viewLifecycleOwner) { show ->
            binding.information.root.isVisible = show
            binding.statisticsTable.isVisible = show.not()
        }

        binding.information.infoButton.setOnClickListener {
            findNavController().navigateUp()
        }

        with(binding.headersRow) {
            totalGames.setOnClickListener { viewModel.sortByGames(it.id) }
            victories.setOnClickListener { viewModel.sortByVictories(it.id) }
            allGamesScore.setOnClickListener { viewModel.sortByAllGamesScore(it.id) }
            victoriesPercent.setOnClickListener { viewModel.sortByVictoriesPercent(it.id) }
            wordsTotal.setOnClickListener { viewModel.sortByWordsTotal(it.id) }
            wordsPerGame.setOnClickListener { viewModel.sortByWordsPerGame(it.id) }
            maxWordsInGame.setOnClickListener { viewModel.sortByMaxWordsInGame(it.id) }
            scorePerGame.setOnClickListener { viewModel.sortByScorePerGame(it.id) }
            maxScoreInGame.setOnClickListener { viewModel.sortByMaxScoreInGame(it.id) }
            allGamesScore.setOnClickListener { viewModel.sortByAllGamesScore(it.id) }
            longestWord.setOnClickListener { viewModel.sortByLongestWord(it.id) }
            mostValuableWord.setOnClickListener { viewModel.sortByMostValuableWord(it.id) }
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