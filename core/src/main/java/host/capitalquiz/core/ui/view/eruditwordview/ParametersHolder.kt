package host.capitalquiz.core.ui.view.eruditwordview

import android.graphics.Paint
import android.graphics.drawable.Drawable

interface ParametersHolder : ColorHolder, Invalidator {
    val x1Color: Int

    val letterBonusesColors: IntArray

    val animateUpdates: Boolean

    val badgeHeight: Int

    val paint: Paint

    val radius: Float

    val thicknessColor: Int

    val asteriskDrawable: Drawable?

    val textColor: Int

    var blockSize: Float

    fun isAllowedChar(char: Char): Boolean

    fun scoreOfChar(char: Char): Int
}