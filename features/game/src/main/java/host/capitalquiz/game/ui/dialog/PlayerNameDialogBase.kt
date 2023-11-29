package host.capitalquiz.game.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.transition.TransitionManager
import host.capitalquiz.core.ui.BottomSheetDialogFragmentWithBorder
import host.capitalquiz.core.ui.collect
import host.capitalquiz.core.ui.requirePreviousFragment
import host.capitalquiz.game.databinding.DialogFragmentAddPlayerBinding
import host.capitalquiz.game.ui.GameViewModel

abstract class PlayerNameDialogBase : BottomSheetDialogFragmentWithBorder() {
    protected val parentViewModel by viewModels<GameViewModel>(ownerProducer = { requirePreviousFragment() })
    private var _binding: DialogFragmentAddPlayerBinding? = null
    protected val binding get() = _binding!!
    override val borderView get() = binding.border

    @get:ColorInt
    abstract val dialogColor: Int

    @get: StringRes
    abstract val dialogTitleRes: Int

    @get: StringRes
    abstract val buttonTextRes: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogFragmentAddPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playerName.requestFocus()
        binding.dialogHeader.setBackgroundColor(dialogColor)
        binding.dialogHeader.setText(dialogTitleRes)
        binding.confirmPlayer.setText(buttonTextRes)

        parentViewModel.playerAddedEvent.collect(viewLifecycleOwner) { event ->
            TransitionManager.beginDelayedTransition(binding.content)
            binding.errorMessage.isVisible = true
            event.consume(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}