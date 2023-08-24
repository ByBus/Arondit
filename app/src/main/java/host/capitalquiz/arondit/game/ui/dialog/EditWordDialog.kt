package host.capitalquiz.arondit.game.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R

@AndroidEntryPoint
class EditWordDialog : BaseWordBottomDialog() {

    private val args by navArgs<EditWordDialogArgs>()
    override val titleRes = R.string.edit_word_dialog_title
    override val confirmButtonTextRes = R.string.save
    override val headerColor get() = args.dialogColor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadWord(args.wordId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.removeWord.apply {
            isVisible = true
            setOnClickListener {
                viewModel.deleteWord(args.wordId)
                dismiss()
            }
        }

        binding.confirmWord.setBackgroundResource(R.drawable.all_corners_straight)
    }
}