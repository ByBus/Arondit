package host.capitalquiz.gameslist.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.core.ui.BindingFragment
import host.capitalquiz.core.ui.BorderDrawable
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.core.ui.collect
import host.capitalquiz.gameslist.R
import javax.inject.Inject
import host.capitalquiz.gameslist.databinding.FragmentGamesListBinding as GamesBinding

@AndroidEntryPoint
class GamesListFragment : BindingFragment<GamesBinding>(), GameAdapter.Callback {

    override val viewInflater: Inflater<GamesBinding> = GamesBinding::inflate
    private val viewModel: GamesListViewModel by viewModels()

    @Inject
    lateinit var navigation: GamesListNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RESULT_REQUEST_CODE) { _, bundle ->
            val gameId = bundle.getLong(REMOVE_GAME_ID_KEY)
            viewModel.removeGame(gameId)
        }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        super.onViewCreated(view, savedInstanceState)
        val gameAdapter = GameAdapter(this)

        binding.gamesList.adapter = gameAdapter
        binding.border.background = BorderDrawable(
            requireContext(),
            R.drawable.medival_corner_2,
            R.drawable.medival_pipe_2,
        ).apply {
            cutPipeEnds(25)
        }

        binding.showStatistics.setOnClickListener {
            viewModel.showStatisticsScreen()
        }

        binding.createGame.setOnClickListener {
            viewModel.createGame()
        }

        binding.information.infoButton.setOnClickListener {
            viewModel.createGame()
        }

        viewModel.games.collect(viewLifecycleOwner) {
            binding.information.root.isVisible = it.isEmpty()
            if (binding.information.root.isVisible) {
                binding.information.infoImage.setImageResource(R.drawable.img_camp)
            }
            binding.gamesLayout.isVisible = it.isNotEmpty()
            gameAdapter.submitList(it)
        }

        viewModel.navigationState.collect(viewLifecycleOwner) { navState ->
            navState.navigate(navigation)
        }

        viewModel.showStatisticsButton.collect(viewLifecycleOwner) {
            binding.showStatistics.isVisible = it
        }
    }

    override fun onGameClick(gameId: Long) = viewModel.showGame(gameId)

    override fun onGameLongClick(gameId: Long) = viewModel.showRemoveGameDialog(gameId)

    override fun onEditGameRule(gameId: Long) = viewModel.showEditGameRuleScreen(gameId)

    companion object {
        const val RESULT_REQUEST_CODE = "games list request code"
        const val REMOVE_GAME_ID_KEY = "remove game with id"
    }
}