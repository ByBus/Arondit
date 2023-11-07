package host.capitalquiz.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import host.capitalquiz.core.databinding.DialogRemoveBinding

abstract class YesNoDialog : BottomSheetDialogFragmentWithBorder() {
    private var _binding: DialogRemoveBinding? = null
    private val binding get() = _binding!!

    abstract val infoMessageText: String
    abstract val confirmButtonText: String
    abstract val cancelButtonText: String

    abstract fun onConfirmDialog()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogRemoveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.text = confirmButtonText
        binding.cancelButton.text = cancelButtonText
        binding.infoMessage.text = infoMessageText

        binding.confirmButton.setOnClickListener {
            onConfirmDialog()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.border.background = CompositeBorderDrawable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}