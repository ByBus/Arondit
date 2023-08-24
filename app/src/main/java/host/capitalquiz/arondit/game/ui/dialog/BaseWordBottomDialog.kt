package host.capitalquiz.arondit.game.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.BottomSheetDialogFragmentWithBorder
import host.capitalquiz.arondit.core.ui.observeFlows
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogHeader.text = getString(titleRes)
        binding.dialogHeader.setBackgroundColor(headerColor)

        viewModel.word.observe(viewLifecycleOwner) { word ->
            with(binding) {
                TransitionManager.beginDelayedTransition(binding.dialogButtons,
                    TransitionSet().apply {
                        ordering = TransitionSet.ORDERING_SEQUENTIAL
                        addTransition(ChangeBounds())
                        addTransition(Fade(Fade.IN))
                    })
                word.update(eruditWord, x2WordBonusButton, x3WordBonusButton, extraPointsButton)
            }
        }

        viewModel.word.observe(viewLifecycleOwner) {
            val editText = binding.wordInput.editText
            if (editText?.text.isNullOrBlank() && it.word.isNotBlank()) {
                editText?.setText(it.word)
            }
        }

        viewModel.definition.observe(viewLifecycleOwner) { definition ->
            definition.update(binding.glossaryBlock)
        }

        binding.eruditWord.setLetterClickListener { index, _ ->
            viewModel.changeLetterScore(index)
        }

        binding.eruditWord.setLetterLongClickListener { index ->
            viewModel.switchLetterAsterisk(index)
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

        binding.extraPointsButton.setOnClickListener {
            viewModel.showExtraScore(binding.extraPointsButton.isChecked)
        }

        binding.confirmWord.text = view.context.getText(confirmButtonTextRes)
        binding.confirmWord.setOnClickListener {
            if (!binding.wordInput.editText?.text.isNullOrBlank()) {
                viewModel.saveWord()
            }
        }

        binding.wordInput.editText?.addTextChangedListener {
            viewModel.updateWord(it.toString())
        }

        binding.border.background = CompositeBorderDrawable()

        observeFlows {
            viewModel.wordSavingResult.collect { saved ->
                if (saved) {
                    dismiss()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.this_word_already_used_message), Snackbar.LENGTH_SHORT
                    ).apply {
                        animationMode = Snackbar.ANIMATION_MODE_SLIDE
                    }.show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}