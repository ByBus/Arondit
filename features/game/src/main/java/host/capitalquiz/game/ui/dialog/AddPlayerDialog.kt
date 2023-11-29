package host.capitalquiz.game.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionManager
import host.capitalquiz.core.ui.BottomSheetDialogFragmentWithBorder
import host.capitalquiz.core.ui.collect
import host.capitalquiz.core.ui.requirePreviousFragment
import host.capitalquiz.game.databinding.DialogFragmentAddPlayerBinding
import host.capitalquiz.game.ui.GameViewModel

class AddPlayerDialog : BottomSheetDialogFragmentWithBorder() {
    private val parentViewModel by viewModels<GameViewModel>(ownerProducer = { requirePreviousFragment() })
    private var _binding: DialogFragmentAddPlayerBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddPlayerDialogArgs>()
    override val borderView get() = binding.border

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel.loadAvailablePlayers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogFragmentAddPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogHeader.setBackgroundColor(args.color)

        binding.confirmPlayer.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.content)
            binding.errorMessage.isVisible = false
            val playerName = binding.playerName.editText?.text
            if (playerName?.isNotBlank() == true) {
                parentViewModel.addPlayer(playerName.toString().trim(), args.color)
            }
        }

        binding.playerName.requestFocus()

        val playersAdapter = PlayersAdapter { playerId ->
            parentViewModel.addPlayer("", args.color, playerId)
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        val spanSizeLookUp = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val itemCount = playersAdapter.itemCount
                return if (itemCount % 2 != 0 && position == itemCount - 1) 2 else 1
            }
        }

        gridLayoutManager.spanSizeLookup = spanSizeLookUp
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL

        binding.availablePlayers.apply {
            adapter = playersAdapter
            layoutManager = gridLayoutManager
        }

        parentViewModel.availablePlayers.observe(viewLifecycleOwner) { players ->
            binding.playersListGroup.isVisible = players.isNotEmpty()
            playersAdapter.submitList(players)
        }

        parentViewModel.playerAddedEvent.collect(viewLifecycleOwner) { event ->
            TransitionManager.beginDelayedTransition(binding.content)
            binding.errorMessage.isVisible = true
            event.consume(this)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        parentViewModel.returnColor(args.color)
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}