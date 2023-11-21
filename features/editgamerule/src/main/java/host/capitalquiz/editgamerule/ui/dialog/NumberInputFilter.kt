package host.capitalquiz.editgamerule.ui.dialog

import android.text.InputFilter
import android.text.Spanned

private const val EMPTY = ""

class NumberInputFilter(private val maxNumber: Int): InputFilter {
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int,
    ): CharSequence {
        return try {
            val newValueString: String = dest.subSequence(0, dstart).toString() +
                    source.subSequence(start, end).toString() +
                    dest.subSequence(dend, dest.length)
            val newValueInt = newValueString.toInt()
            if (newValueInt >= 0 && isInRange(0, maxNumber, newValueInt)) source else EMPTY
        } catch (e: NumberFormatException) {
            EMPTY
        }
    }

    private fun isInRange(min: Int, max: Int, value: Int): Boolean {
        return if (max > min) value in min..max else value in max..min
    }
}