package host.capitalquiz.arondit.core.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import host.capitalquiz.arondit.databinding.DialogRemoveGameBinding

abstract class YesNoDialog : BottomSheetDialogFragmentWithBorder() {
    private var _binding: DialogRemoveGameBinding? = null
    protected val binding get() = _binding!!

    abstract val infoMessageText: String
    abstract val confirmButtonText: String
    abstract val cancelButtonText: String

    abstract fun onConfirmDialog()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogRemoveGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}