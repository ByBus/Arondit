package host.capitalquiz.arondit.gameslist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.databinding.FragmentGamesListBinding

@AndroidEntryPoint
class GamesListFragment : Fragment() {
    private val viewModel: GamesListViewModel by viewModels()
    private var _binding: FragmentGamesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGamesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = GameAdapter { gameId ->
            findNavController().navigate(GamesListFragmentDirections.actionToGameFragment(gameId))
            Log.i("GamesListFragment", "onViewCreated: $gameId")
        }
        binding.gamesList.adapter = adapter

        binding.createGame.setOnClickListener {
            viewModel.createGame()
        }

        viewModel.games.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

//        viewModel.navigateToGameScreen.observe(viewLifecycleOwner) {
//            if (it) {
//                viewModel.resetNavigation()
//                val gameId = viewModel.newGameId.value!!
//                findNavController()
//                    .navigate(
//                        GamesListFragmentDirections.actionToGameFragment(gameId)
//                    )
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}