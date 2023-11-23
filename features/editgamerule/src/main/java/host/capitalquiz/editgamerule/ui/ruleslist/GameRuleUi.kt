package host.capitalquiz.editgamerule.ui.ruleslist

import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView

data class GameRuleUi(
    val id: Long,
    val name: String,
    val points: Map<Char, Int>,
    val readOnly: Boolean,
    val gamesIds: List<Long>,
    val selected: Boolean
) {
    fun update(radio: RadioButton, ruleName: TextView, deleteButton: ImageButton) {
        radio.isChecked = selected
        ruleName.text = name
        deleteButton.isEnabled = readOnly.not()
        deleteButton.imageAlpha = if (readOnly) 75 else 255
    }
}