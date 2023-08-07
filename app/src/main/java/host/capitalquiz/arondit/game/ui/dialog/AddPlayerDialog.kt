package host.capitalquiz.arondit.game.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import host.capitalquiz.arondit.databinding.DialogFragmentAddPlayerBinding
import host.capitalquiz.arondit.game.domain.Player
import host.capitalquiz.arondit.game.ui.GameViewModel

class AddPlayerDialog: DialogFragment() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window
            ?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        binding.playerName.requestFocus()

        binding.dialogHeader.setBackgroundColor(args.color)

        binding.confirmPlayer.setOnClickListener {
            val playerName = binding.playerName.editText?.text.toString()
            if (playerName.isNotBlank()) {
                parentViewModel.addPlayer(Player(name = playerName, color = args.color))
                dismiss()
            }
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }
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