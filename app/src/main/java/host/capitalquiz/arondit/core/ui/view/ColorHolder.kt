package host.capitalquiz.arondit.core.ui.view

import android.graphics.drawable.Drawable

interface ColorHolder : LetterColorHolder, WordColorHolder

interface LetterColorHolder {
    val x2LetterDrawable: Drawable?
    val x3LetterDrawable: Drawable?

    val baseColor: Int
    val x2LetterColor: Int
    val x3LetterColor: Int
}

interface WordColorHolder {
    val x2Drawable: Drawable?
    val x3Drawable: Drawable?

    val x2Color: Int
    val x3Color: Int
}