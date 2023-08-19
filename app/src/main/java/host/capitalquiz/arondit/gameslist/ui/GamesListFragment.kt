package host.capitalquiz.arondit.gameslist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.BorderDrawable
import host.capitalquiz.arondit.databinding.FragmentGamesListBinding

@AndroidEntryPoint
class GamesListFragment : Fragment(), GameAdapter.Callback {
    private val viewModel: GamesListViewModel by viewModels()
    private var _binding: FragmentGamesListBinding? = null
    private val binding get() = _binding!!
    private val gameAdapter = GameAdapter(object : GameAdapter.Callback {
        override fun onGameClick(gameId: Long) =
            this@GamesListFragment.onGameClick(gameId)

        override fun onGameLongClick(gameId: Long) =
            this@GamesListFragment.onGameLongClick(gameId)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(REMOVE_GAME_REQUEST_CODE) { _, bundle ->
            val gameId = bundle.getLong(REMOVE_GAME_ID_KEY)
            viewModel.removeGame(gameId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGamesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        viewModel.navigateToGameScreen.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.resetNavigation()
                val gameId = viewModel.newGameId.value!!
                navigateToGame(gameId)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onGameClick(gameId: Long) {
        navigateToGame(gameId)
    }

    override fun onGameLongClick(gameId: Long) {
        findNavController()
            .navigate(GamesListFragmentDirections.actionToRemoveGameDialog(gameId))
    }

    private fun navigateToGame(gameId: Long) {
        findNavController()
            .navigate(GamesListFragmentDirections.actionToGameFragment(gameId))
    }


    companion object {
        const val REMOVE_GAME_REQUEST_CODE = "remove game"
        const val REMOVE_GAME_ID_KEY = "remove game with id"
    }
}