package host.capitalquiz.arondit.game.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R


@AndroidEntryPoint
class AddWordDialog : BaseWordBottomDialog() {
    private val args by navArgs<AddWordDialogArgs>()
    override val titleRes = R.string.add_word_dialog_title
    override val confirmButtonTextRes = R.string.add_word_button
    override val headerColor get() = args.dialogColor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initWord(args.playerId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.wordInput.requestFocus()
    }
}

