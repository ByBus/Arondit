package host.capitalquiz.core.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.ColorUtils
import host.capitalquiz.core.R
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val SLEEVE_GRADIENT_OVERSIZE = 1.1f
private const val SLEEVE_CURVED_SIDE_W_TO_H_RATIO = 0.15f

class FlagView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private var stripePadding = 0f
    private var stripeWidth = 0f
    private var rodDiameter = 0f
    private var flagColor = 0
    private var stripeColor = 0
    private var rodGradient: Shader? = null
    private var sleeveGradient: Shader? = null
    private var clothGradient: Shader? = null
    private val sleeveSize get() = rodDiameter * 1.5f
    private var straightPartFraction = 0.6f
    private var sleevesCount = 0
    private var centerColor = 0
    private var shadowColor = 0
    private val path = Path()
    private val leftSleeveCurvedEdge = Path()
    private val rightSleeveCurvedEdge = Path()
    private var maxWidth = 0f
    private val overlayMode = PorterDuffXfermode(PorterDuff.Mode.OVERLAY)
    private var leftFlagBorder = 0f
    private var rightFlagBorder = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.FlagView) {
            stripePadding = getDimension(R.styleable.FlagView_stripePadding, 20f)
            stripeWidth = getDimension(R.styleable.FlagView_stripeWidth, 20f)
            flagColor = getColor(R.styleable.FlagView_flagColor, Color.RED)
            stripeColor = getColor(R.styleable.FlagView_stripeColor, Color.GRAY)
            rodDiameter = getDimension(R.styleable.FlagView_horizontalRodDiameter, 4f)
            sleevesCount = getInt(R.styleable.FlagView_sleevesCount, 3)
            centerColor = getColor(R.styleable.FlagView_centerColor, 0)
            straightPartFraction =
                getFloat(R.styleable.FlagView_topToBottomRatio, 0.6f).coerceIn(0f, 1f)
            shadowColor = getColor(R.styleable.FlagView_shadowColor, 0)
            maxWidth = getDimension(R.styleable.FlagView_flagMaxWidth, 0f)
        }
    }

    private val stripePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = stripeWidth
        color = stripeColor
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
    }


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)


        if (shadowColor != 0) paint.setShadowLayer(8f, 0f, 4f, shadowColor)
        paint.color = flagColor
        paint.xfermode = null

        // rod
        paint.shader = rodGradient
        val gap = (sleeveSize - rodDiameter) / 2
        canvas?.drawRect(
            0f,
            paddingTop.toFloat() + gap,
            width.toFloat(),
            paddingTop + gap + rodDiameter,
            paint
        )

        // base of flag
        val verticalFirst = height * straightPartFraction
        val verticalMiddle = (height - paddingBottom).toFloat()
        val horizontalMiddle = (leftFlagBorder + rightFlagBorder) / 2f
        paint.shader = null
        path.apply {
            rewind()
            moveTo(leftFlagBorder, sleeveSize)
            lineTo(leftFlagBorder, verticalFirst)
            lineTo(horizontalMiddle, verticalMiddle)
            lineTo(rightFlagBorder, verticalFirst)
            lineTo(rightFlagBorder, sleeveSize)
            close()
        }

        val basePath = Path(path)

        val availableWidth = availableWidth()
        val sleeveLength =
            (if (sleevesCount == 1) availableWidth else availableWidth / (sleevesCount * 2 - 1))

        // sleeves
        var leftPosition = leftFlagBorder
        val topY = paddingTop.toFloat()
        val bottomY = paddingTop + sleeveSize
        val edgeWidth = sleeveSize * SLEEVE_CURVED_SIDE_W_TO_H_RATIO
        repeat(sleevesCount) loop@{
            val rightPosition = leftPosition + sleeveLength
            path.addRect(
                leftPosition,
                topY,
                rightPosition,
                bottomY,
                Path.Direction.CCW
            )
            //left sleeve curved side
            if (it == 0) {
                val size = leftPosition - edgeWidth
                val edge = createSleeveEdge(leftPosition, topY, size, bottomY, true)
                path.addPath(edge)
            }
            //right sleeve curved side
            if (it == sleevesCount - 1) {
                val size = rightPosition + edgeWidth
                val edge = createSleeveEdge(rightPosition, topY, size, bottomY, false)
                path.addPath(edge)
            }
            leftPosition = rightPosition + sleeveLength
        }
        canvas?.drawPath(path, paint)

        // inner stripe
        val lengthA = verticalFirst - sleeveSize
        val lengthB = length(leftFlagBorder, verticalFirst, horizontalMiddle, verticalMiddle)
        val lengthC = length(leftFlagBorder, sleeveSize, horizontalMiddle, verticalMiddle)
        val angleC = angleACBRad(lengthA, lengthB, lengthC)
        val deltaYOfStripeC = stripePadding * cos(angleC / 2)

        val lengthD = length(leftFlagBorder, verticalFirst, rightFlagBorder, verticalFirst)
        val angleBottom = angleACBRad(lengthB, lengthB, lengthD)
        val deltaYOfStripeBottom = stripePadding / sin(angleBottom / 2)

        path.apply {
            rewind()
            moveTo(leftFlagBorder + stripePadding, sleeveSize + stripePadding)
            lineTo(leftFlagBorder + stripePadding, verticalFirst - deltaYOfStripeC)
            lineTo(horizontalMiddle, verticalMiddle - deltaYOfStripeBottom)
            lineTo(rightFlagBorder - stripePadding, verticalFirst - deltaYOfStripeC)
            lineTo(rightFlagBorder - stripePadding, sleeveSize + stripePadding)
            close()
        }

        // stripe's inner room fill
        if (centerColor != 0) {
            paint.color = centerColor
            paint.clearShadowLayer()
            canvas?.drawPath(path, paint)
            paint.color = flagColor
        }

        canvas?.drawPath(path, stripePaint)

        // sleeves gradient
        paint.shader = sleeveGradient
        paint.xfermode = overlayMode
        canvas?.drawPath(leftSleeveCurvedEdge, paint)
        canvas?.drawPath(rightSleeveCurvedEdge, paint)
        leftPosition = leftFlagBorder
        val bottomPosition = paddingTop + sleeveSize * SLEEVE_GRADIENT_OVERSIZE
        repeat(sleevesCount) {
            val rightPosition = leftPosition + sleeveLength
            canvas?.drawRect(
                leftPosition,
                topY,
                rightPosition,
                bottomPosition,
                paint
            )
            leftPosition = rightPosition + sleeveLength
        }

        paint.shader = clothGradient
        canvas?.drawPath(basePath, paint)
    }

    private fun availableWidth(): Float {
        return if (maxWidth > 0 && maxWidth < width)
            maxWidth
        else
            (width - paddingLeft - paddingRight).toFloat()
    }

    private fun left(): Float {
        return if (maxWidth > 0 && maxWidth < width)
            (width - maxWidth) / 2
        else
            paddingLeft.toFloat()
    }

    private fun right(): Float {
        return if (maxWidth > 0 && maxWidth < width)
            (width - maxWidth) / 2 + maxWidth
        else
            (width - paddingRight).toFloat()
    }

    private fun createSleeveEdge(
        flatEdgeX: Float,
        topY: Float,
        sizeWithDirection: Float,
        bottomY: Float,
        isLeft: Boolean,
    ): Path {
        val sleeve = if (isLeft) leftSleeveCurvedEdge else rightSleeveCurvedEdge
        return sleeve.apply {
            rewind()
            moveTo(flatEdgeX, topY)
            cubicTo(
                sizeWithDirection,
                topY,
                sizeWithDirection,
                bottomY,
                flatEdgeX,
                bottomY
            )
            close()
        }
    }

    private fun length(x0: Float, y0: Float, x1: Float, y1: Float): Float =
        sqrt((x1 - x0).pow(2) + (y1 - y0).pow(2))

    private fun angleACBRad(a: Float, b: Float, c: Float): Float =
        acos((a * a + b * b - c * c) / (2 * a * b))

    private fun createRodGradient(): Shader {
        val colors =
            intArrayOf(0x888783, 0xa4a393, 0xddd7b6, 0x919292, 0x4c4950, 0x615f67, 0xcfcfc2)
                .map {
                    ColorUtils.setAlphaComponent(it, 255)
                }
        val positions = floatArrayOf(0f, .12f, .29f, .47f, .71f, .85f, 1f)
        val gap = (sleeveSize - rodDiameter) / 2
        return LinearGradient(
            0f,
            paddingTop.toFloat() + gap,
            0f,
            paddingTop + gap + rodDiameter,
            colors.toIntArray(),
            positions,
            Shader.TileMode.CLAMP
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        leftFlagBorder = left()
        rightFlagBorder = right()
        rodGradient = createRodGradient()
        sleeveGradient = createSleeveGradient()
        clothGradient = createClothGradient()
    }

    private fun createSleeveGradient(): Shader {
        val colors = intArrayOf(0x888888, 0x969696, 0xBBBBBB, 0x727272, 0x5A5A5A, 0x828282).map {
            ColorUtils.setAlphaComponent(it, 255)
        }
        val positions = floatArrayOf(0f, .03f, 0.25f, .55f, .88f, 1f)
        return LinearGradient(
            0f,
            paddingTop.toFloat(),
            0f,
            paddingTop + sleeveSize * SLEEVE_GRADIENT_OVERSIZE,
            colors.toIntArray(),
            positions,
            Shader.TileMode.CLAMP
        )
    }

    private fun createClothGradient(): Shader {
        val colors = listOf<Long>(0, 0x15a2a2a2, 0x25f5f5f5, 0x158b8b8b, 0).map {
            it.toInt()
        }
        val positions = floatArrayOf(0f, .28f, .54f, .8f, 1f)
        return LinearGradient(
            0f,
            0f,
            150f,
            0f,
            colors.toIntArray(),
            positions,
            Shader.TileMode.MIRROR
        )
    }
}