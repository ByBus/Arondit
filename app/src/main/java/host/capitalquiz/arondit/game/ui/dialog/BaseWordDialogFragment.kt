package host.capitalquiz.arondit.game.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import host.capitalquiz.arondit.databinding.DialogFragmentAddWordBinding

abstract class BaseWordDialogFragment: DialogFragment() {
    protected val viewModel by viewModels<WordDialogViewModel>()
    private var _binding: DialogFragmentAddWordBinding? = null
    protected val binding get() = _binding!!

    @get:StringRes
    abstract val titleRes: Int
    @get:ColorInt
    abstract val headerColor: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogFragmentAddWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window
            ?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        binding.dialogHeader.text = view.context.getText(titleRes)
        binding.dialogHeader.setBackgroundColor(headerColor)
        binding.cancel.setOnClickListener { dismiss() }
        binding.wordInput.requestFocus()

        binding.wordInput.editText?.addTextChangedListener {
            viewModel.updateWord(it.toString())
        }

        viewModel.word.observe(viewLifecycleOwner) {
            binding.eruditWord.setText(it.word)
            binding.eruditWord.multiplier = it.multiplier
            binding.x2WordBonusButton.isChecked = it.multiplier == 2
            binding.x3WordBonusButton.isChecked = it.multiplier == 3
        }

        binding.x2WordBonusButton.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateWordMultiplier(if (isChecked) 2 else 1)
        }
        binding.x3WordBonusButton.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateWordMultiplier(if (isChecked) 3 else 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}