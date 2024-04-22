package host.capitalquiz.core.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import host.capitalquiz.core.R
import host.capitalquiz.core.ui.view.CompositeBorderDrawable

abstract class BottomSheetDialogFragmentWithBorder : BottomSheetDialogFragment(), Dismissible {

    protected abstract val borderView: View

    private fun CompositeBorderDrawable() = CompositeBorderDrawable.baseDialog(requireContext())
        .apply {
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
        borderView.background = CompositeBorderDrawable()
        view.postDelayed({
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED)
        }, 500)
    }

    protected fun Snackbar(@StringRes resId: Int): Snackbar =
        Snackbar.make(
            requireView(),
            getString(resId),
            Snackbar.LENGTH_SHORT
        ).apply {
            animationMode = Snackbar.ANIMATION_MODE_SLIDE
        }
}