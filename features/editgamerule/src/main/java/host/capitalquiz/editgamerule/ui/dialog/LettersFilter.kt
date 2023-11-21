package host.capitalquiz.editgamerule.ui.dialog

import android.text.InputFilter
import android.text.Spanned


class LettersFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence {
        return (source?.lastOrNull {
            it.isDigit().not() && it.isWhitespace().not()
        }?.uppercase() ?: "").toString()

    }
}