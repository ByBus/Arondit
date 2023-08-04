package host.capitalquiz.arondit.game.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import host.capitalquiz.arondit.databinding.DialogFragmentAddWordBinding

class AddWordDialog : DialogFragment() {
    private val parentViewModel by viewModels<GameViewModel>(ownerProducer = { requirePreviousFragment() })
    private var _binding: DialogFragmentAddWordBinding? = null
    private val binding get() = _binding!!
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
        binding.cancel.setOnClickListener { dismiss() }
        binding.confirmWord.setOnClickListener {
            parentViewModel.addWord()
            dismiss()
        }

        val eruditWord = binding.eruditWord
        binding.wordInput.editText?.addTextChangedListener {
            parentViewModel.cacheWord(it.toString())
        }

        parentViewModel.tempWord.observe(viewLifecycleOwner) {
            eruditWord.setText(it)
        }

        val x3Btn = binding.x3WordBonusButton
        val x2Btn = binding.x2WordBonusButton
        x2Btn.setOnCheckedChangeListener { _, isChecked ->
            eruditWord.multiplier = (if (isChecked) {
                x3Btn.isChecked = false
                2
            } else 1)
        }
        x3Btn.setOnCheckedChangeListener { _, isChecked ->
            eruditWord.multiplier = (if (isChecked) {
                x2Btn.isChecked = false
                3
            } else 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

private fun Fragment.requirePreviousFragment(): Fragment {
    val fragments = requireParentFragment().childFragmentManager.fragments
    return if (fragments.size < 2) requireParentFragment() else fragments[fragments.lastIndex - 1]
}