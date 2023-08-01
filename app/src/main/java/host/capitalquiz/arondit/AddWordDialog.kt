package host.capitalquiz.arondit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import host.capitalquiz.arondit.databinding.DialogFragmentAddWordBinding

class AddWordDialog : DialogFragment() {
    private val parentViewModel by viewModels<GameViewModel>(ownerProducer = { requireParentFragment() })
    private var _binding: DialogFragmentAddWordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

        }
        binding.wordInput.editText?.addTextChangedListener {
            binding.eruditWord.setText(it.toString())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}