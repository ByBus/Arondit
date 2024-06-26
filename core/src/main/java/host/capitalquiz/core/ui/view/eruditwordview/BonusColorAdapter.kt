package host.capitalquiz.core.ui.view.eruditwordview

import android.graphics.drawable.Drawable

class BonusColorAdapter(
    private val wordColorHolder: WordColorHolder,
    initColor: Int
) : LetterColorHolder {
    override val x2LetterDrawable: Drawable? get() = wordColorHolder.x2Drawable
    override val x3LetterDrawable: Drawable? get() = wordColorHolder.x3Drawable
    override val baseColor: Int = initColor
    override val x2LetterColor: Int get() = wordColorHolder.x2Color
    override val x3LetterColor: Int get() = wordColorHolder.x3Color
}