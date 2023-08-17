package host.capitalquiz.arondit.game.ui.dialog

import android.content.res.ColorStateList
import android.graphics.Color
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.view.DialogSymmetricalBorderDrawable
import host.capitalquiz.arondit.databinding.DialogFragmentAddWordBinding


@AndroidEntryPoint
abstract class BaseWordBottomDialog : BottomSheetDialogFragment() {

    protected val viewModel by viewModels<WordDialogViewModel>()
    private var _binding: DialogFragmentAddWordBinding? = null
    protected val binding get() = _binding!!

    @get:StringRes
    abstract val titleRes: Int

    @get:ColorInt
    abstract val headerColor: Int

    @get:StringRes
    abstract val confirmButtonTextRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

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
//        dialog?.window
//            ?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
//        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
//        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        binding.dialogHeader.text = view.context.getText(titleRes)
//        binding.header.setBackgroundColor(headerColor)
        binding.dialogHeader.setBackgroundColor(headerColor)

        //Remove background
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.elevation = 0f

        binding.wordInput.editText?.addTextChangedListener {
            viewModel.updateWord(it.toString())
        }

        viewModel.word.observe(viewLifecycleOwner) { word ->
            with(binding) {
//                TransitionManager.beginDelayedTransition(binding.content)
                word.update(eruditWord, x2WordBonusButton, x3WordBonusButton)
            }
        }

        viewModel.definition.observe(viewLifecycleOwner){ definition ->
//            TransitionManager.beginDelayedTransition(binding.content)
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

        viewModel.word.observe(viewLifecycleOwner){
            val editText = binding.wordInput.editText
            if (editText?.text.isNullOrBlank() && it.word.isNotBlank()) {
                editText?.setText(it.word)
            }
        }

        binding.border.background = DialogSymmetricalBorderDrawable(
            requireContext(),
            leftTopCorner = R.drawable.dialog_border_top_left_corner,
            leftBottomCorner = R.drawable.dialog_border_bottom_left_corner,
            leftVerticalPipe = R.drawable.dialog_border_vertical_pipe,
            topHorizontalPipe = R.drawable.dialog_border_top_hor_pipe,
            bottomHorizontalPipe = R.drawable.dialog_border_horizontal_pipe,
            topHorizontalDecorTile = R.drawable.dialog_border_top_hor_pipe_pattern
        ).apply {
            moveDecorSides(-15, 15)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}