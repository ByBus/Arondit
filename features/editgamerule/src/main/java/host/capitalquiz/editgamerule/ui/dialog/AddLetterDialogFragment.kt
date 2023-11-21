package host.capitalquiz.editgamerule.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.core.ui.BottomSheetDialogFragmentWithBorder
import host.capitalquiz.core.ui.collect
import host.capitalquiz.editgamerule.R
import host.capitalquiz.editgamerule.databinding.FragmentAddLetterDialogBinding
import host.capitalquiz.editgamerule.ui.editscreen.EditGameRuleFragment
import javax.inject.Inject

@AndroidEntryPoint
class AddLetterDialogFragment : BottomSheetDialogFragmentWithBorder(), Dismissible {
    private var _binding: FragmentAddLetterDialogBinding? = null
    private val binding get() = _binding!!
    override val borderView get() = binding.border
    private val args by navArgs<AddLetterDialogFragmentArgs>()

    @Inject
    lateinit var editLetterVMFactory: EditLetterViewModelFactory

    private val viewModel: EditLetterViewModel by viewModels {
        EditLetterViewModel.factory(editLetterVMFactory, args.ruleId)
    }

    private val letter get() = binding.letterInput.editText!!.text.first()
    private val points get() = binding.pointsInput.editText!!.text.toString().toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.letter?.let { letter ->
            viewModel.initLetter(letter.first(), args.points)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddLetterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val letterInput = binding.letterInput.editText!!
        val pointsInput = binding.pointsInput.editText!!

        letterInput.filters += LettersFilter()
        pointsInput.filters += NumberInputFilter(100)

        EmptinessWatcher(letterInput, pointsInput) { isEmpty ->
            with(binding) {
                okButton.isEnabled = isEmpty.not()
                addLetterButton.isEnabled = isEmpty.not()
                replaceButton.isEnabled = isEmpty.not()
            }
        }

        viewModel.init(getString(R.string.copy_rule_prefix_name))

        viewModel.addLetterUiState.collect(viewLifecycleOwner) { uiState ->
            uiState.update(binding, this)
        }

        viewModel.latestRuleId.collect(viewLifecycleOwner) { ruleId ->
            setFragmentResult(
                EditGameRuleFragment.REQUEST_KEY,
                bundleOf(EditGameRuleFragment.RULE_ID_KEY to ruleId)
            )
        }

        binding.okButton.setOnClickListener {
            viewModel.saveLetter(letter, points, true)
        }

        binding.replaceButton.setOnClickListener {
            viewModel.saveLetter(
                letter,
                points,
                closeAfter = false,
                replace = true
            )
        }

        binding.addLetterButton.setOnClickListener {
            viewModel.saveLetter(letter, points, false)
        }

        binding.letterInput.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
