package host.capitalquiz.arondit.game.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.databinding.FragmentGameBinding


@AndroidEntryPoint
class GameFragment : Fragment() {
    private val viewModel: GameViewModel by viewModels()
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var gridLayoutAdapter: GridLayoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridLayoutAdapter = GridLayoutAdapter(requireContext(),
            addUserCallBack = { addPlayer() },
            removeUserCallback = { playerId, playerColor ->
                viewModel.returnColor(playerColor.value)
                viewModel.deletePlayer(playerId.value)
            },
            openAddWordDialogCallback = { playerId, playerColor ->
                findNavController()
                    .navigate(
                        GameFragmentDirections.actionAddWordDialog(
                            playerId.value,
                            playerColor.value
                        )
                    )
            },
            wordClickCallback = { wordId, playerColor, playerId ->
                val editWordDialog =
                    GameFragmentDirections.actionToEditWordDialog(
                        wordId,
                        playerColor.value,
                        playerId.value
                    )
                findNavController().navigate(editWordDialog)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.addColors(
            listOf(
                R.color.player_color_1,
                R.color.player_color_2,
                R.color.player_color_3,
                R.color.player_color_4
            ).map {
                ContextCompat.getColor(requireContext(), it)
            })
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridLayoutAdapter.apply {
            bindTo(binding.grid)
        }

        with(binding.information){
            infoButton.setOnClickListener {
                addPlayer()
            }
            infoText.text = getString(R.string.no_players_info_text)
            infoImage.setImageResource(R.drawable.knights)
            infoButton.text = getString(R.string.add_participant)
        }

        viewModel.players.observe(viewLifecycleOwner) { players ->
            viewModel.removeUsedColors(players.map { it.color })
            binding.information.root.isVisible = players.isEmpty()
            binding.grid.isVisible = players.isNotEmpty()
            gridLayoutAdapter.submitList(players)
        }
    }

    private fun addPlayer() {
        viewModel.borrowColor { color ->
            val addWordDialog = GameFragmentDirections.actionToAddPlayerDialog(color)
            findNavController().navigate(addWordDialog)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}