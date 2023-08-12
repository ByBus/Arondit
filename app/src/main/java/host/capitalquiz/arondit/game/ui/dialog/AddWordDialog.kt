package host.capitalquiz.arondit.game.ui.dialog

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R

@AndroidEntryPoint
class AddWordDialog : BaseWordDialogFragment() {
    private val args by navArgs<AddWordDialogArgs>()
    override val titleRes = R.string.add_word_dialog_title
    override val headerColor get() = args.dialogColor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initWord(args.playerId)
    }
}

