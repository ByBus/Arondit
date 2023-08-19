package host.capitalquiz.arondit.gameslist.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.core.ui.BottomSheetDialogFragmentWithBorder
import host.capitalquiz.arondit.databinding.DialogRemoveGameBinding

@AndroidEntryPoint
class RemoveGameDialog: BottomSheetDialogFragmentWithBorder() {
    private var _binding: DialogRemoveGameBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<RemoveGameDialogArgs>()

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

        binding.removeGame.setOnClickListener {
            setFragmentResult(
                GamesListFragment.REMOVE_GAME_REQUEST_CODE,
                bundleOf(GamesListFragment.REMOVE_GAME_ID_KEY to args.gameId))
            dismiss()
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