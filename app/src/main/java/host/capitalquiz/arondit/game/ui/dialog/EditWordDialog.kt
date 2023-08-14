package host.capitalquiz.arondit.game.ui.dialog

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
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

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        binding.confirmWord.setBackgroundResource(R.drawable.left_corners_round)

        viewModel.word.observe(viewLifecycleOwner){
            val editText = binding.wordInput.editText
            if (editText?.text.isNullOrBlank() && it.word.isNotBlank()) {
                editText?.setText(it.word)
            }
        }
    }
}