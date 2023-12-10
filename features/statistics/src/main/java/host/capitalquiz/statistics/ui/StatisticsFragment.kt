package host.capitalquiz.statistics.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.core.ui.BindingFragment
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.core.ui.collect
import host.capitalquiz.statistics.R
import javax.inject.Inject
import host.capitalquiz.statistics.databinding.FragmentStatisticsBinding as StatisticsBinding

@AndroidEntryPoint
class StatisticsFragment : BindingFragment<StatisticsBinding>() {

    private val viewModel: StatisticsViewModel by viewModels()
    override val viewInflater: Inflater<StatisticsBinding> = StatisticsBinding::inflate

    @Inject
    lateinit var navigation: StatisticsNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadStatistics()

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        super.onViewCreated(view, savedInstanceState)

        val userNameAdapter =
            UserNameAdapter(R.color.even_player_row_color, R.color.odd_player_row_color)
        val table = binding.table

        table.columnPlayersNames.apply {
            adapter = userNameAdapter
            setHasFixedSize(true)
        }

        val userStatsAdapter = UserStatsAdapter(R.color.even_row_color, R.color.odd_row_color)
        table.statisticsRows.apply {
            adapter = userStatsAdapter
            setHasFixedSize(true)
        }

        viewModel.sortedRows.collect(viewLifecycleOwner) { items ->
//            val items = it.flatMap { itms ->List(30) {itms} }
            userNameAdapter.submitList(items.map { it.playerName })
            userStatsAdapter.submitList(items)
        }

        viewModel.headersState.collect(viewLifecycleOwner) { state ->
            state.update(table.headersRow.columnNames)
        }

        viewModel.showInformation.collect(viewLifecycleOwner) { show ->
            with(binding) {
                information.root.isVisible = show
                table.statisticsTable.isVisible = show.not()
                appBarLayout.isVisible = show.not()
                if (show) {
                    information.infoImage.setImageResource(R.drawable.joker)
                    information.infoText.setText(R.string.no_statistics_yet)
                    information.infoButton.setText(R.string.close)
                }
            }
        }

        binding.information.infoButton.setOnClickListener {
            navigation.navigateUp()
        }

        binding.toolbar.setNavigationOnClickListener {
            navigation.navigateUp()
        }

        with(table.headersRow) {
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
            table.headersHorizontalScroller,
            table.statisticsHorizontalScroller,
            table.columnPlayersNames,
            table.statisticsRows,
            table.motionLayoutRoot
        )

        userNameAdapter.registerAdapterDataObserver(
            ScrollToPositionObserver(
                table.columnPlayersNames,
                0
            )
        )
        userStatsAdapter.registerAdapterDataObserver(
            ScrollToPositionObserver(
                table.statisticsRows,
                0
            )
        )
    }
}