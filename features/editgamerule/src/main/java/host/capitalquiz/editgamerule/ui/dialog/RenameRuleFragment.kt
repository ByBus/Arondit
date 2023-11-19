package host.capitalquiz.editgamerule.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import host.capitalquiz.core.ui.BottomSheetDialogFragmentWithBorder
import host.capitalquiz.editgamerule.databinding.FragmentRenameRuleBinding
import host.capitalquiz.editgamerule.ui.editscreen.EditGameRuleFragment


class RenameRuleFragment : BottomSheetDialogFragmentWithBorder() {
    private var _binding: FragmentRenameRuleBinding? = null
    private val binding get() = _binding!!
    override val borderView get() = binding.border
    private val args by navArgs<RenameRuleFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRenameRuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gameName.editText?.setText(args.ruleName)

        binding.gameName.editText?.doOnTextChanged { text, _, _, _ ->
            binding.renameButton.isEnabled = text?.isNotBlank() ?: false
        }

        binding.renameButton.setOnClickListener {
            val ruleName = binding.gameName.editText!!.text.toString()
            setFragmentResult(
                EditGameRuleFragment.REQUEST_KEY,
                bundleOf(EditGameRuleFragment.NAME_KEY to ruleName.trim())
            )
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}