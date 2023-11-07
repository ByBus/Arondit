package host.capitalquiz.core.ui.view.eruditwordview

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import host.capitalquiz.core.R

class Parameters(
    context: Context,
    canvasHolder: Invalidator
) : ParametersHolder, Invalidator by canvasHolder {
    override val x1Color = WORD_X1_COLOR
    override val x2Color = WORD_X2_COLOR
    override val x3Color = WORD_X3_COLOR
    override val x2LetterColor: Int get() = letterBonusesColors[2]
    override val x3LetterColor: Int get() = letterBonusesColors[3]
    override val baseColor: Int get() = letterBonusesColors[1]
    override var animateUpdates: Boolean = false
    override var blockSize: Float = 0f
    override var badgeHeight: Int = 0
    override var radius: Float = 0f
    override var textColor: Int = 0
    // index = bonus: 0 is for asterisk color, 1 is for base color...
    override val letterBonusesColors = intArrayOf(0, 0, LETTER_X2_COLOR, LETTER_X3_COLOR)
    override val thicknessColor: Int = (0x70000000).toInt()
    override val x2Drawable by lazy { R.drawable.ic_bonus_x2_strict_24.loadDrawable(context) }
    override val x3Drawable by lazy { R.drawable.ic_bonus_x3_strict_24.loadDrawable(context) }
    override val x2LetterDrawable by lazy { R.drawable.ic_letter_bonus_x2_strict_24.loadDrawable(context) }
    override val x3LetterDrawable by lazy { R.drawable.ic_letter_bonus_x3_strict_24.loadDrawable(context) }
    override val asteriskDrawable by lazy { R.drawable.ic_asterisk6_curved_24.loadDrawable(context) }
    override val paint = Paint()
        .apply {
            strokeWidth = 2f
            isAntiAlias = true
        }

    override fun isAllowedChar(char: Char): Boolean = scoresDictionary.containsKey(char.uppercaseChar())

    override fun scoreOfChar(char: Char): Int = scoresDictionary[char.uppercaseChar()] ?: 0

    companion object {
        private const val LETTER_X2_COLOR = 0xFF26A69A.toInt()
        private const val LETTER_X3_COLOR = 0xFFFBC02D.toInt()
        private const val WORD_X2_COLOR = 0xFF42A5F5.toInt()
        private const val WORD_X3_COLOR = 0xFFEF5350.toInt()
        private const val WORD_X1_COLOR = Color.TRANSPARENT
        private val scoresDictionary = mapOf(
            'А' to 1, 'Б' to 3, 'В' to 2, 'Г' to 3, 'Д' to 2, 'Е' to 1, 'Ё' to 1, 'Ж' to 5,
            'З' to 5, 'И' to 1, 'Й' to 2, 'К' to 2, 'Л' to 3, 'М' to 2, 'Н' to 1, 'О' to 1,
            'П' to 2, 'Р' to 2, 'С' to 2, 'Т' to 2, 'У' to 3, 'Ф' to 10, 'Х' to 5, 'Ц' to 10,
            'Ч' to 5, 'Ш' to 10, 'Щ' to 10, 'Ъ' to 10, 'Ы' to 5, 'Ь' to 5, 'Э' to 10, 'Ю' to 10,
            'Я' to 3, '*' to 0
        )
    }
}
private fun @receiver:DrawableRes Int.loadDrawable(context: Context) =
    ContextCompat.getDrawable(context, this)
