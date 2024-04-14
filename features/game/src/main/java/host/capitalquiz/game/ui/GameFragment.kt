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
import dagger.hilt.android.lifecycle.withCreationCallback
import host.capitalquiz.core.ui.BindingFragment
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.core.ui.collect
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

    private val viewModel: GameViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras
                .withCreationCallback<GameViewModelFactory> { factory ->
                    factory.create(args.gameId)
                }
        }
    )

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
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
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

        gridLayoutAdapter?.bindTo(binding.grid)

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

        viewModel.navigationEvent.collect(viewLifecycleOwner) { navEvent ->
            navEvent.navigate(navigation)
        }
    }

    override fun onDestroyView() {
        gridLayoutAdapter = null
        super.onDestroyView()
    }

    override fun onAddPlayerClick() = viewModel.goToAddPlayerDialog()

    override fun onRemovePlayerClick(fieldId: FieldId, fieldColor: FieldColor) =
        viewModel.goToRemovePlayerDialog(fieldId.value, fieldColor.value)

    override fun onAddWordClick(fieldId: FieldId, fieldColor: FieldColor) =
        viewModel.goToAddWordDialog(fieldId.value, fieldColor.value)

    override fun onWordClick(wordId: Long, fieldId: FieldId, fieldColor: FieldColor) =
        viewModel.goToEditWordDialog(wordId, fieldId.value, fieldColor.value)

    override fun onNameClick(name: String, playerId: Long, fieldColor: FieldColor) =
        viewModel.goToRenamePlayerDialog(name, playerId, fieldColor.value)

    companion object {
        const val RESULT_REQUEST_CODE = "host.capitalquiz.game current game request code"
        const val REMOVE_PLAYER_ID_KEY = "host.capitalquiz.game remove player with id"
        const val REMOVE_PLAYER_COLOR_KEY = "host.capitalquiz.game return player's color"
    }
}