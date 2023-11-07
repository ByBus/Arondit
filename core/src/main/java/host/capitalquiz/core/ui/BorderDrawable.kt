package host.capitalquiz.core.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation


class BorderDrawable(
    context: Context,
    @DrawableRes val topLeftCorner: Int,
    @DrawableRes val horizontalPipe: Int,
    @DrawableRes val middleDecor: Int? = null,
) : Drawable() {
    private var cornerXShift = 0
    private var cornerYShift = 0
    private var pipeCutEnd = 0
    private var pipeYShift = 0F
    private val pipeBitmap = BitmapFactory.decodeResource(context.resources, horizontalPipe)
    private val cornerBitmap = BitmapFactory.decodeResource(context.resources, topLeftCorner)
    private val pipe = pipeBitmap.toDrawable(context.resources)
    private val corner = cornerBitmap.toDrawable(context.resources)
    private val pipeBounds = Rect().apply {
        bottom = pipeBitmap.height
    }
    private val cornerBounds = Rect()

    //MiddleTop Decor
    private val decorBitmap = middleDecor?.let {
        BitmapFactory.decodeResource(context.resources, it)
    }
    private val decor = decorBitmap?.toDrawable(context.resources)

    override fun draw(canvas: Canvas) {
        val isWide = bounds.width() > bounds.height()
        val longSideLength = Math.max(bounds.width(), bounds.height())
        val shortSideLength = Math.min(bounds.width(), bounds.height())

        pipe.tileModeX = Shader.TileMode.REPEAT

        corner.bounds = cornerBounds.apply {
            left = cornerXShift
            top = cornerYShift
            right = cornerBitmap.width + cornerXShift
            bottom = cornerBitmap.height + cornerYShift
        }

        repeat(4) { side: Int ->
            pipe.bounds = pipeBounds.apply {
                right =
                    (if (isWide xor (side and 1 == 0)) shortSideLength else longSideLength) - pipeCutEnd
            }
            val x = if (side == LEFT) bounds.exactCenterY() else bounds.exactCenterX()
            val y = if (side == RIGHT) bounds.exactCenterX() else bounds.exactCenterY()
            canvas.withRotation(90F * side, x, y) {
                canvas.withTranslation(y = pipeYShift) {
                    pipe.draw(canvas)
                }
                if (side != TOP) corner.draw(canvas)
            }
        }
        corner.draw(canvas)

        decor?.bounds?.apply {
            val leftSidePosition = (bounds.width() - decorBitmap!!.width) / 2
            bottom = pipeBounds.bottom
            left = leftSidePosition
            right = leftSidePosition + decorBitmap.width
        }
        decor?.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity() = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    fun shiftCorner(x: Int, y: Int) {
        cornerXShift = x
        cornerYShift = y
    }

    fun shiftPipeY(y: Int) {
        pipeYShift = y.toFloat()
    }

    fun cutPipeEnds(value: Int) {
        pipeBounds.left = value
        pipeCutEnd = value
    }

    companion object {
        private const val TOP = 0
        private const val RIGHT = 1
        private const val BOTTOM = 2
        private const val LEFT = 3
    }
}