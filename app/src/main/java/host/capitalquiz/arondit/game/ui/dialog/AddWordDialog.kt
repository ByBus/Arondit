package host.capitalquiz.arondit.game.ui.dialog

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R


@AndroidEntryPoint
class AddWordDialog : BaseWordBottomDialog() {
    private val args by navArgs<AddWordDialogArgs>()
    override val titleRes = R.string.add_word_dialog_title
    override val confirmButtonTextRes = R.string.add_word
    override val headerColor get() = args.dialogColor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initWord(args.playerId)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        dialog?.window?.setSoftInputMode(
//            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE or
////                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
//                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding.wordInput.requestFocus()
//
//        binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
//                binding.root.setPadding(0, 0, 0, imeHeight * 2)
//            }
//            windowInsets
//        }

    }
}

