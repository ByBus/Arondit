package host.capitalquiz.arondit.core.ui.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TextInputTypeWriter @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : TextInputEditText(context, attrs) {
    private val scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null
    private var message = ""

    fun type(message: String, paceMs: Long) {
        job?.cancel()
        this.message = message
        job = scope.launch {
            var current = 0
            while (current <= message.length) {
                setText(message.substring(0, current++))
                delay(paceMs)
            }
        }
    }

    fun erase(paceMs: Long) {
        job?.cancel()
        message = text?.toString() ?: ""
        job = scope.launch {
            var current = message.length
            while (current > 0) {
                setText(message.substring(0, --current))
                delay(paceMs)
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        message = ""
        setText(message)
    }
}
