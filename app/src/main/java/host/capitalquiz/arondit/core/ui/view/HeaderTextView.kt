package host.capitalquiz.arondit.core.ui.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.withTranslation
import host.capitalquiz.arondit.R
import kotlin.math.roundToInt


class HeaderTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : AppCompatTextView(context, attrs) {
    private lateinit var leftDrawable: Drawable
    private lateinit var centerDrawable: Drawable
    private lateinit var rightDrawable: Drawable
    private var leftTintDrawable: Drawable? = null
    private var rightTintDrawable: Drawable? = null

    private val paint = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.FILL
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.HeaderTextView) {
            leftDrawable = getDrawable(R.styleable.HeaderTextView_leftDrawable)
                ?: ColorDrawable(Color.TRANSPARENT)
            centerDrawable = getDrawable(R.styleable.HeaderTextView_centerDrawable)
                ?: ColorDrawable(Color.TRANSPARENT)
            rightDrawable = getDrawable(R.styleable.HeaderTextView_rightDrawable)
                ?: ColorDrawable(Color.TRANSPARENT)
            rightTintDrawable = getDrawable(R.styleable.HeaderTextView_rightDrawableTintBackgroundMask)
            leftTintDrawable = getDrawable(R.styleable.HeaderTextView_leftDrawableTintBackgroundMask)
        }

        if (background is ColorDrawable) {
            val bgColor = (background as ColorDrawable).color
            paint.color = bgColor
            updateTint(bgColor, leftTintDrawable)
            updateTint(bgColor, rightTintDrawable)
            (background as ColorDrawable).color = Color.TRANSPARENT
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)


        val desiredWidth =
            measuredWidth + desiredWidthOf(leftDrawable) + desiredWidthOf(rightDrawable)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize // Must be this size
            MeasureSpec.AT_MOST -> Math.min(desiredWidth, widthSize) // Can't be bigger than
            else -> desiredWidth // Be whatever we want
        }

        setMeasuredDimension(width, measuredHeight)
    }

    private fun desiredWidthOf(drawable: Drawable): Int =
        (drawable.intrinsicWidth * (measuredHeight.toFloat() / drawable.intrinsicHeight))
            .roundToInt()

    override fun setBackgroundColor(color: Int) {
        paint.color = color
        updateTint(color, leftTintDrawable)
        updateTint(color, rightTintDrawable)
        invalidate()
    }

    private fun updateTint(color: Int, drawable: Drawable?) {
        if (drawable == null) return
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable, color)
    }

    override fun onDraw(canvas: Canvas) {
        leftDrawable.let {
            it.setBounds(0, 0, desiredWidthOf(it), measuredHeight)
        }
        rightDrawable.let {
            it.setBounds(width - desiredWidthOf(it), 0, width, measuredHeight)
        }
        centerDrawable.setBounds(
            leftDrawable.bounds.right,
            0,
            rightDrawable.bounds.left,
            measuredHeight
        )
        canvas.drawRect(centerDrawable.bounds, paint)
        canvas.withTranslation(x = leftDrawable.bounds.right.toFloat()) {
            super.onDraw(canvas)
        }
        leftTintDrawable?.apply {
            bounds = leftDrawable.bounds
            draw(canvas)
        }
        leftDrawable.draw(canvas)

        rightTintDrawable?.apply {
            bounds = rightDrawable.bounds
            draw(canvas)
        }
        rightDrawable.draw(canvas)

        centerDrawable.draw(canvas)
    }

}