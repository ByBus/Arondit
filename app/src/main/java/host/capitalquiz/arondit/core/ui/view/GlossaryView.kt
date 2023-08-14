package host.capitalquiz.arondit.core.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import host.capitalquiz.arondit.R


private const val ANIMATION_DURATION = 200L

class GlossaryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : RelativeLayout(context, attrs) {
    private var wordTerm: TextView
    private var glossary: TextView
    private var definition: TextView
    private val animationInterpolator = DecelerateInterpolator()
    private var previousHeight = 0

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.glossary_view_layout, this, true)
        wordTerm = view.findViewById<TextView>(R.id.wordTerm).also { it.text = "" }
        glossary = view.findViewById<TextView>(R.id.wordGlossary).also { it.text = "" }
        definition = view.findViewById<TextView>(R.id.wordDefinition).also { it.text = "" }
    }

    private val heightAnimator = ValueAnimator.ofInt(0, 0).apply {
        interpolator = animationInterpolator
        duration = ANIMATION_DURATION
        addUpdateListener {
            definition.height = it.animatedValue as Int
        }
        doOnEnd {
            definition.layoutParams = definition.layoutParams.apply {
                height = LayoutParams.WRAP_CONTENT
            }
        }
    }

    fun setWithAnimation(word: String, glossaryTitle: String, wordDefinition: String) {
        if (wordTerm.text == word) return
        if (word.isBlank()) {
            set(word, glossaryTitle, wordDefinition)
            isVisible  = false
            return
        } else {
            isVisible = true
        }
        hideDefinition {
            set(word, glossaryTitle, wordDefinition)
            showDefinition()
        }
    }

    fun set(word: String, glossaryTitle: String, wordDefinition: String) {
        if (wordTerm.text == word) return
        wordTerm.text = word
        glossary.text = glossaryTitle
        definition.text = wordDefinition
    }

    private fun hideDefinition(onEnd: () -> Unit) {
        previousHeight = definition.height
        animate()
            .alpha(0f)
            .translationX(40f)
            .setDuration(ANIMATION_DURATION)
            .setInterpolator(animationInterpolator)
            .withEndAction {
                onEnd.invoke()
            }
            .start()
    }

    private fun showDefinition() {
        heightAnimator.setIntValues(previousHeight, heightOf(definition))
        heightAnimator.start()
        translationX = 0f
        animate()
            .alpha(1f)
            .setDuration(ANIMATION_DURATION)
            .setInterpolator(animationInterpolator)
            .start()
    }

    private fun heightOf(textView: TextView): Int {
        val dummyTextView = TextView(context).apply {
            setPadding(this@GlossaryView.paddingLeft, 0, this@GlossaryView.paddingRight, 0)
            text = textView.text
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.textSize)
            typeface = textView.typeface
        }
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(
            this.width - this.paddingLeft - this.paddingRight,
            MeasureSpec.AT_MOST
        )
        dummyTextView.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED)
        return dummyTextView.measuredHeight
    }

}