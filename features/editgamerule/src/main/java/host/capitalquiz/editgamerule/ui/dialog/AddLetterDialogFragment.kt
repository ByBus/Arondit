package host.capitalquiz.editgamerule.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import host.capitalquiz.core.ui.BottomSheetDialogFragmentWithBorder
import host.capitalquiz.editgamerule.databinding.FragmentAddLetterDialogBinding


class AddLetterDialogFragment : BottomSheetDialogFragmentWithBorder() {
    private var _binding: FragmentAddLetterDialogBinding? = null
    private val binding get() = _binding!!
    override val borderView get() = binding.border

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddLetterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.letterInput.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
