package host.capitalquiz.arondit.game.ui.dialog

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import host.capitalquiz.core.R
import host.capitalquiz.arondit.game.ui.GameFragment
import host.capitalquiz.core.ui.YesNoDialog

class RemovePlayerDialog : YesNoDialog() {
    private val args by navArgs<RemovePlayerDialogArgs>()
    override val infoMessageText: String get() = getString(R.string.remove_player_info_message)
    override val confirmButtonText: String get() = getString(R.string.remove_player_button)
    override val cancelButtonText: String get() = getString(R.string.cancel_button)

    override fun onConfirmDialog() {
        setFragmentResult(
            GameFragment.RESULT_REQUEST_CODE,
            bundleOf(
                GameFragment.REMOVE_PLAYER_ID_KEY to args.playerId,
                GameFragment.REMOVE_PLAYER_COLOR_KEY to args.playerColor
            )
        )
        dismiss()
    }
}
