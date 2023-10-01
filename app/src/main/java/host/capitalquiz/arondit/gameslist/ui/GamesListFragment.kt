package host.capitalquiz.arondit.gameslist.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.BindingFragment
import host.capitalquiz.arondit.core.ui.BorderDrawable
import host.capitalquiz.arondit.core.ui.Inflater
import host.capitalquiz.arondit.core.ui.observeFlows
import host.capitalquiz.arondit.databinding.FragmentGamesListBinding as GamesBinding

@AndroidEntryPoint
class GamesListFragment : BindingFragment<GamesBinding>(), GameAdapter.Callback,
    GamesListFragmentNavigation {

    override val viewInflater: Inflater<GamesBinding> = GamesBinding::inflate
    private val viewModel: GamesListViewModel by viewModels()
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

        binding.createGame.setOnClickListener {
            viewModel.createGame()
        }

        binding.information.infoButton.setOnClickListener {
            viewModel.createGame()
        }

        viewModel.games.observe(viewLifecycleOwner) {
            binding.information.root.isVisible = it.isEmpty()
            binding.gamesLayout.isVisible = it.isNotEmpty()
            gameAdapter.submitList(it)
        }

        observeFlows {
            viewModel.navigationState.collect { navState ->
                navState.navigate(this)
            }
        }
    }

    override fun onGameClick(gameId: Long) = viewModel.showGame(gameId)

    override fun onGameLongClick(gameId: Long) = viewModel.showRemoveGameDialog(gameId)

    override fun navigateToRemoveGameDialog(gameId: Long) {
        findNavController()
            .navigate(GamesListFragmentDirections.actionToRemoveGameDialog(gameId))
    }

    override fun navigateToGame(gameId: Long) {
        findNavController()
            .navigate(GamesListFragmentDirections.actionToGameFragment(gameId))
    }

    override fun navigateToOnBoarding(gameId: Long) {
        findNavController()
            .navigate(GamesListFragmentDirections.actionToOnBoardingFragment())
    }

    companion object {
        const val RESULT_REQUEST_CODE = "games list request code"
        const val REMOVE_GAME_ID_KEY = "remove game with id"
    }
}