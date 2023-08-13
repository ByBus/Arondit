package host.capitalquiz.arondit.game.ui.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.databinding.DialogFragmentAddWordBinding

@AndroidEntryPoint
abstract class BaseWordBottomDialog : BottomSheetDialogFragment() {

    protected val viewModel by viewModels<WordDialogViewModel>()
    private var _binding: DialogFragmentAddWordBinding? = null
    protected val binding get() = _binding!!

    @get:StringRes
    abstract val titleRes: Int
    @get:ColorInt
    abstract val headerColor: Int
    @get:StringRes
    abstract val confirmButtonTextRes: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogFragmentAddWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        dialog?.window
//            ?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
//        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
//        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.dialogHeader.text = view.context.getText(titleRes)
        binding.dialogHeader.setBackgroundColor(headerColor)

        binding.wordInput.editText?.addTextChangedListener {
            viewModel.updateWord(it.toString())
        }

        viewModel.word.observe(viewLifecycleOwner) {wordUi ->
            with(binding){
                wordUi.update(eruditWord, x2WordBonusButton, x3WordBonusButton)
            }
        }

        binding.eruditWord.setLetterClickListener{ index, _ ->
            viewModel.changeLetterScore(index)
        }

        with(binding.x2WordBonusButton){
            setOnClickListener {
                viewModel.updateWordMultiplier(if (isChecked) 2 else 1)
            }
        }

        with(binding.x3WordBonusButton){
            setOnClickListener {
                viewModel.updateWordMultiplier(if (isChecked) 3 else 1)
            }
        }

        binding.confirmWord.text = view.context.getText(confirmButtonTextRes)
        binding.confirmWord.setOnClickListener {
            if (!binding.wordInput.editText?.text.isNullOrBlank()) {
                viewModel.saveWord()
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}