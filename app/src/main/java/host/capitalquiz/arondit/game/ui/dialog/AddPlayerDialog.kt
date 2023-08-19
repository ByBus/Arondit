package host.capitalquiz.arondit.game.ui.dialog

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import host.capitalquiz.arondit.core.ui.BottomSheetDialogFragmentWithBorder
import host.capitalquiz.arondit.databinding.DialogFragmentAddPlayerBinding
import host.capitalquiz.arondit.game.domain.Player
import host.capitalquiz.arondit.game.ui.GameViewModel

class AddPlayerDialog: BottomSheetDialogFragmentWithBorder() {
    private val parentViewModel by viewModels<GameViewModel>(ownerProducer = { requirePreviousFragment() })
    private var _binding: DialogFragmentAddPlayerBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddPlayerDialogArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogFragmentAddPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogHeader.setBackgroundColor(args.color)

        binding.confirmPlayer.setOnClickListener {
            val playerName = binding.playerName.editText?.text.toString()
            if (playerName.isNotBlank()) {
                parentViewModel.addPlayer(Player(name = playerName, color = args.color))
                dismiss()
            }
        }

        binding.border.background = CompositeBorderDrawable()

        binding.playerName.requestFocus()
    }

    override fun onDismiss(dialog: DialogInterface) {
        parentViewModel.returnColor(args.color)
        super.onDismiss(dialog)
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