package host.capitalquiz.arondit.core.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import host.capitalquiz.arondit.R
import host.capitalquiz.arondit.core.ui.view.CompositeBorderDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BottomSheetDialogFragmentWithBorder : BottomSheetDialogFragment() {

    fun CompositeBorderDrawable() = CompositeBorderDrawable(
        requireContext(),
        leftTopCorner = R.drawable.dialog_border_top_left_corner,
        leftVerticalPipe = R.drawable.dialog_border_vertical_pipe,
        leftBottomCorner = R.drawable.dialog_border_bottom_left_corner,
        bottomHorizontalPipe = R.drawable.dialog_border_horizontal_pipe,
        topHorizontalPipe = R.drawable.dialog_border_top_hor_pipe,
        topHorizontalDecorTile = R.drawable.dialog_border_top_hor_pipe_pattern
    ).apply {
        moveDecorSides(-15, 15)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Remove background
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.elevation = 0f
    }
}

inline fun Fragment.observeFlows(crossinline observationFunction: suspend (CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycle.coroutineScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            observationFunction(this)
        }
    }
}