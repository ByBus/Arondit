package host.capitalquiz.game.ui.dialog

import android.text.InputFilter
import android.text.Spanned

class NoSpacesFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence? = source?.filterNot { char -> char.isWhitespace() }
}