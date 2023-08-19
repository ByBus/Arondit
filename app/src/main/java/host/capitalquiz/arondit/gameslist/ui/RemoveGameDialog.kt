package host.capitalquiz.arondit.gameslist.ui

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.YesNoDialog

@AndroidEntryPoint
class RemoveGameDialog : YesNoDialog() {
    private val args by navArgs<RemoveGameDialogArgs>()

    override val infoMessageText: String get() = getString(R.string.remove_game_info_message)
    override val confirmButtonText: String get() = getString(R.string.remove_game_button)
    override val cancelButtonText: String get() = getString(R.string.cancel_button)
    override fun onConfirmDialog() {
        setFragmentResult(
            GamesListFragment.RESULT_REQUEST_CODE,
            bundleOf(GamesListFragment.REMOVE_GAME_ID_KEY to args.gameId)
        )
        dismiss()
    }
}