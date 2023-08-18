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
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.databinding.DialogFragmentAddWordBinding


@AndroidEntryPoint
abstract class BaseWordBottomDialog : BottomSheetDialogFragmentWithBorder() {

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
        binding.dialogHeader.text = getString(titleRes)
        binding.dialogHeader.setBackgroundColor(headerColor)

        viewModel.word.observe(viewLifecycleOwner) { word ->
            with(binding) {
//                TransitionManager.beginDelayedTransition(binding.content)
                word.update(eruditWord, x2WordBonusButton, x3WordBonusButton)
            }
        }

        viewModel.word.observe(viewLifecycleOwner){
            val editText = binding.wordInput.editText
            if (editText?.text.isNullOrBlank() && it.word.isNotBlank()) {
                editText?.setText(it.word)
            }
        }

        viewModel.definition.observe(viewLifecycleOwner){ definition ->
            definition.update(binding.glossaryBlock)
        }

        binding.eruditWord.setLetterClickListener { index, _ ->
            viewModel.changeLetterScore(index)
        }

        with(binding.x2WordBonusButton) {
            setOnClickListener {
                viewModel.updateWordMultiplier(if (isChecked) 2 else 1)
            }
        }

        with(binding.x3WordBonusButton) {
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

        binding.wordInput.editText?.addTextChangedListener {
            viewModel.updateWord(it.toString())
        }

        binding.border.background = CompositeBorderDrawable()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}