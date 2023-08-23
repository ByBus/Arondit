package host.capitalquiz.arondit.core.ui.view.eruditwordview

import android.graphics.Paint
import android.graphics.drawable.Drawable

interface ParametersHolder : ColorHolder, Invalidator {

    val letterBonusesColors: IntArray

    val animateUpdates: Boolean

    val badgeHeight: Int

    val paint: Paint

    val radius: Float

    val thicknessColor: Int

    val asteriskDrawable: Drawable?

    val textColor: Int
    fun isAllowedChar(char: Char): Boolean
    fun scoreOfChar(char: Char): Int
}