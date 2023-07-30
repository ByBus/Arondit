package host.capitalquiz.arondit

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val availableColors = mutableListOf<Int>()
    val words = listOf(
        "Слово",
        "ЭКскаватор",
        "Велосипед",
        "Ме*та",
        "покрышк*",
        "шомпол",
        "чтец",
        "шмотка",
        "Щука"
    )

    fun addColors(colors: List<Int>) {
        availableColors.addAll(colors)
    }

    fun borrowColor(): Int {
        return availableColors.removeLast()
    }

    fun returnColor(color: Int) {
        availableColors.add(color)
    }
}