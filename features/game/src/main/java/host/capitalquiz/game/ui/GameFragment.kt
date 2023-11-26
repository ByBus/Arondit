package host.capitalquiz.game.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.core.ui.BindingFragment
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.core.ui.collect
import host.capitalquiz.core.ui.view.CompositeBorderDrawable
import host.capitalquiz.game.R
import host.capitalquiz.game.databinding.FragmentGameBinding
import javax.inject.Inject
import host.capitalquiz.core.R as RCore


@AndroidEntryPoint
class GameFragment : BindingFragment<FragmentGameBinding>(), GridLayoutAdapter.Listener {
    override val viewInflater: Inflater<FragmentGameBinding> = FragmentGameBinding::inflate
    private var gridLayoutAdapter: GridLayoutAdapter? = null
    private val args by navArgs<GameFragmentArgs>()

    @Inject
    lateinit var navigation: GameNavigation

    @Inject
    lateinit var gameViewModelFactory: GameViewModelFactory

    private val viewModel: GameViewModel by viewModels{
        GameViewModel.provideFactory(gameViewModelFactory, args.gameId)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridLayoutAdapter = GridLayoutAdapter(this)

        setFragmentResultListener(RESULT_REQUEST_CODE) { _, bundle ->
            viewModel.deletePlayer(bundle.getLong(REMOVE_PLAYER_ID_KEY))
            val playerColor = bundle.getInt(REMOVE_PLAYER_COLOR_KEY)
            viewModel.returnColor(playerColor)
            gridLayoutAdapter?.removeField(playerColor)
        }

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.addFieldsColors(
            listOf(
                RCore.color.base_orange,
                RCore.color.base_green,
                RCore.color.base_red,
                RCore.color.base_blue
            ).map {
                ContextCompat.getColor(requireContext(), it)
            })

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        super.onViewCreated(view, savedInstanceState)

        gridLayoutAdapter?.apply {
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
                onAddPlayerClick()
            }
            infoText.text = getString(R.string.no_players_info_text)
            infoImage.setImageResource(R.drawable.img_knights_battle)
            infoButton.text = getString(R.string.add_participant)
        }

        viewModel.fields.collect(viewLifecycleOwner) { players ->
            viewModel.removeUsedColors(players.map { it.color })
            binding.information.root.isVisible = players.isEmpty()
            binding.grid.isVisible = players.isNotEmpty()
            gridLayoutAdapter?.submitList(requireContext(), players)
        }
    }

    override fun onDestroyView() {
        gridLayoutAdapter = null
        super.onDestroyView()
    }

    override fun onAddPlayerClick() {
        viewModel.borrowColor { color ->
            navigation.navigateToToAddPlayerDialog(color)
        }
    }

    override fun onRemovePlayerClick(fieldId: FieldId, fieldColor: FieldColor) {
        navigation.navigateToRemovePlayerDialog(fieldId.value, fieldColor.value)
    }

    override fun onAddWordClick(fieldId: FieldId, fieldColor: FieldColor) {
        navigation.navigateToAddWordDialog(fieldId.value, fieldColor.value)
    }

    override fun onWordClick(wordId: Long, fieldId: FieldId, fieldColor: FieldColor) {
        navigation.navigateToEditWordDialog(wordId, fieldId.value, fieldColor.value)
    }

    companion object {
        const val RESULT_REQUEST_CODE = "current game request code"
        const val REMOVE_PLAYER_ID_KEY = "remove player with id"
        const val REMOVE_PLAYER_COLOR_KEY = "return player's color"
    }
}