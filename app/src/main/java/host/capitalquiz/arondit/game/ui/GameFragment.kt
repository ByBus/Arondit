package host.capitalquiz.arondit.game.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.BindingFragment
import host.capitalquiz.arondit.core.ui.Inflater
import host.capitalquiz.arondit.core.ui.view.CompositeBorderDrawable
import host.capitalquiz.arondit.databinding.FragmentGameBinding
import host.capitalquiz.arondit.game.ui.dialog.GameDialogs


@AndroidEntryPoint
class GameFragment : BindingFragment<FragmentGameBinding>(), GameDialogs {
    override val viewInflater: Inflater<FragmentGameBinding> = FragmentGameBinding::inflate
    private val viewModel: GameViewModel by viewModels()

    private lateinit var gridLayoutAdapter: GridLayoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridLayoutAdapter = GridLayoutAdapter(requireContext(),
            addUserCallBack = { openAddPlayerDialog() },
            removeUserCallback = { playerId, playerColor ->
                openRemovePlayerDialog(playerId.value, playerColor.value)
            },
            openAddWordDialogCallback = { playerId, playerColor ->
                openAddWordDialog(playerId.value, playerColor.value)
            },
            wordClickCallback = { wordId, playerColor, playerId ->
                openEditWordDialog(wordId, playerId.value, playerColor.value)
            }
        )
        setFragmentResultListener(RESULT_REQUEST_CODE) { _, bundle ->
            viewModel.deletePlayer(bundle.getLong(REMOVE_PLAYER_ID_KEY))
            val playerColor = bundle.getInt(REMOVE_PLAYER_COLOR_KEY)
            viewModel.returnColor(playerColor)
            gridLayoutAdapter.removeField(playerColor)
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.addPlayersColors(
            listOf(
                R.color.base_orange,
                R.color.base_green,
                R.color.base_red,
                R.color.base_blue
            ).map {
                ContextCompat.getColor(requireContext(), it)
            })

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        super.onViewCreated(view, savedInstanceState)

        gridLayoutAdapter.apply {
            bindTo(binding.grid)
            addDecorationDrawable(
                CompositeBorderDrawable(
                    requireContext(),
                    leftTopCorner = R.drawable.player_border_top_left_corner,
                    leftBottomCorner = R.drawable.player_border_bottom_left_corner,
                    leftVerticalPipe = R.drawable.player_border_left_vert_pipe,
                    topHorizontalPipe = R.drawable.player_border_top_hor_pipe,
                    bottomHorizontalPipe = R.drawable.player_border_top_hor_pipe
                )
            )
        }

        with(binding.information) {
            infoButton.setOnClickListener {
                openAddPlayerDialog()
            }
            infoText.text = getString(R.string.no_players_info_text)
            infoImage.setImageResource(R.drawable.img_knights_battle)
            infoButton.text = getString(R.string.add_participant)
        }

        viewModel.players.observe(viewLifecycleOwner) { players ->
            viewModel.removeUsedColors(players.map { it.color })
            binding.information.root.isVisible = players.isEmpty()
            binding.grid.isVisible = players.isNotEmpty()
            gridLayoutAdapter.submitList(players)
        }
    }

    override fun openRemovePlayerDialog(playerId: Long, playerColor: Int) {
        findNavController()
            .navigate(GameFragmentDirections.actionToRemovePlayerDialog(playerId, playerColor))
    }

    override fun openAddWordDialog(playerId: Long, playerColor: Int) {
        findNavController()
            .navigate(GameFragmentDirections.actionAddWordDialog(playerId, playerColor))
    }

    override fun openEditWordDialog(wordId: Long, playerId: Long, dialogColor: Int) {
        val editWordDialog =
            GameFragmentDirections.actionToEditWordDialog(
                wordId,
                dialogColor,
                playerId
            )
        findNavController().navigate(editWordDialog)
    }

    override fun openAddPlayerDialog() {
        viewModel.borrowColor { color ->
            val addPlayerDialog = GameFragmentDirections.actionToAddPlayerDialog(color)
            findNavController().navigate(addPlayerDialog)
        }
    }

    companion object {
        const val RESULT_REQUEST_CODE = "current game request code"
        const val REMOVE_PLAYER_ID_KEY = "remove player with id"
        const val REMOVE_PLAYER_COLOR_KEY = "return player's color"
    }
}