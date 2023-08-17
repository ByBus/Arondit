package host.capitalquiz.arondit.core.ui.view

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation

class DialogSymmetricalBorderDrawable(
    context: Context,
    @DrawableRes private val leftTopCorner: Int,
    @DrawableRes private val leftBottomCorner: Int,
    @DrawableRes private val leftVerticalPipe: Int,
    @DrawableRes private val topHorizontalPipe: Int,
    @DrawableRes private val bottomHorizontalPipe: Int,
    @DrawableRes private val topHorizontalDecorTile: Int? = null,
) : Drawable() {
    private val lTCorner = ContextCompat.getDrawable(context, leftTopCorner) ?: ColorDrawable(
        Color.TRANSPARENT
    )
    private val lBCorner = ContextCompat.getDrawable(context, leftBottomCorner) ?: ColorDrawable(
        Color.TRANSPARENT
    )
    private val horPipeTop = topHorizontalPipe.toBitmapDrawableTiled(context.resources)
    private val horPipeBottom = bottomHorizontalPipe.toBitmapDrawableTiled(context.resources)
    private val verticalPipe = leftVerticalPipe.toBitmapDrawableTiled(context.resources, false)
    private val topDecor = topHorizontalDecorTile?.toBitmapDrawableTiled(context.resources)

    private var decorLeftMove = 0
    private var decorRightMove = 0

    private val viewWidth get() = bounds.width()
    private val viewHeight get() = bounds.height()

    fun moveDecorSides(left: Int, right: Int) {
        decorLeftMove = left
        decorRightMove = right
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        lTCorner.let { it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight) }
        lBCorner.let { it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight) }
        verticalPipe.let {
            it.setBounds(
                0, lTCorner.bounds.bottom, it.intrinsicWidth, viewHeight - lBCorner.bounds.width()
            )
        }
        topDecor?.let {
            it.setBounds(
                lTCorner.bounds.width() + decorLeftMove,
                0,
                viewWidth - lTCorner.bounds.width() + decorRightMove,
                it.intrinsicHeight
            )
            it.draw(canvas)
        }
        drawLeftSide(canvas)

        canvas.withMirror {
            drawLeftSide(canvas)
        }

        horPipeTop.let {
            it.setBounds(
                lTCorner.bounds.width(),
                0,
                viewWidth - lTCorner.bounds.width(),
                it.intrinsicHeight
            )
            it.draw(canvas)
        }

        canvas.withRotation(180f, pivotX = viewWidth / 2f, pivotY = viewHeight / 2f) {
            horPipeBottom.let {
                val cornerWidth = lBCorner.bounds.width()
                it.setBounds(
                    cornerWidth,
                    0,
                    viewWidth - cornerWidth,
                    it.intrinsicHeight
                )
                it.draw(canvas)
            }
        }
    }

    private fun drawLeftSide(canvas: Canvas) {
        lTCorner.draw(canvas)
        lBCorner.apply {
            canvas.withTranslation(y = ((viewHeight - intrinsicWidth).toFloat())) {
                draw(canvas)
            }
        }
        verticalPipe.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity() = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {}

}


private fun @receiver:DrawableRes Int.toBitmapDrawableTiled(
    res: Resources,
    horizontal: Boolean = true,
): BitmapDrawable {
    return BitmapFactory.decodeResource(res, this)
        .toDrawable(res).apply {
            if (horizontal)
                tileModeX = Shader.TileMode.REPEAT
            else
                tileModeY = Shader.TileMode.REPEAT
        }
}

private inline fun Canvas.withMirror(
    block: Canvas.() -> Unit,
) {
    scale(-1f, 1f, width / 2f, height / 2f)
    try {
        block()
    } finally {
        scale(-1f, 1f, width / 2f, height / 2f)
    }
}