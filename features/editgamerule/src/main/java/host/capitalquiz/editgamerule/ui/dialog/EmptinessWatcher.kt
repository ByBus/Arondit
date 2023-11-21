package host.capitalquiz.editgamerule.ui.dialog

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class EmptinessWatcher(
    private vararg val editText: EditText,
    private val isAnyEmptyListener: (Boolean) -> Unit,
) : TextWatcher {
    init {
        editText.forEach { edT ->
            edT.addTextChangedListener(this)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        isAnyEmptyListener(editText.any { it.text.isNullOrBlank() })
    }

    override fun afterTextChanged(s: Editable?) = Unit
}