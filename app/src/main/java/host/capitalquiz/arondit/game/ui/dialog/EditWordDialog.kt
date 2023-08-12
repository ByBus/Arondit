package host.capitalquiz.arondit.game.ui.dialog

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R

@AndroidEntryPoint
class EditWordDialog : BaseWordDialogFragment() {

    private val args by navArgs<EditWordDialogArgs>()
    override val titleRes = R.string.edit_word_dialog_title
    override val headerColor get() = args.dialogColor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadWord(args.wordId)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogHeader.setBackgroundColor(args.dialogColor)
        binding.removeWord.apply {
            isVisible = true
            setOnClickListener {
                viewModel.deleteWord(args.wordId)
                dismiss()
            }
        }

        binding.confirmWord.setOnClickListener {
            viewModel.updateWord(args.playerId)
            dismiss()
        }

        viewModel.word.observe(viewLifecycleOwner){
            val editText = binding.wordInput.editText
            if (editText?.text.isNullOrBlank() && it.word.isNotBlank()) {
                editText?.setText(it.word)
            }
        }
    }
}