package host.capitalquiz.game.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import host.capitalquiz.game.R

class RenamePlayerDialog : PlayerNameDialogBase() {
    private val args by navArgs<RenamePlayerDialogArgs>()
    override val dialogColor: Int get() = args.playerColor
    override val dialogTitleRes: Int = R.string.rename_player_header
    override val buttonTextRes: Int = R.string.save

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playerNameEditText = binding.playerName.editText
        playerNameEditText?.append(args.oldName)

        binding.confirmPlayer.setOnClickListener {
            val newName = playerNameEditText?.text
            if (newName.isNullOrBlank().not()) {
                parentViewModel.renamePlayer(newName.toString(), args.playerId)
            }
        }
    }
}