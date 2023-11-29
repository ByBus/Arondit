package host.capitalquiz.game.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionManager
import host.capitalquiz.game.R

class AddPlayerDialog : PlayerNameDialogBase() {
    private val args by navArgs<AddPlayerDialogArgs>()
    override val dialogColor: Int get() = args.color
    override val dialogTitleRes: Int = R.string.new_player_header
    override val buttonTextRes: Int = R.string.add_player_button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel.loadAvailablePlayers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmPlayer.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.content)
            binding.errorMessage.isVisible = false
            val playerName = binding.playerName.editText?.text
            if (playerName?.isNotBlank() == true) {
                parentViewModel.addPlayer(playerName.toString().trim(), args.color)
            }
        }

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
    }

    override fun onDismiss(dialog: DialogInterface) {
        parentViewModel.returnColor(args.color)
        super.onDismiss(dialog)
    }
}