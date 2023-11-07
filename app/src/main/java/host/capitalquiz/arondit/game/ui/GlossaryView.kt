package host.capitalquiz.arondit.game.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import host.capitalquiz.arondit.R


private const val ANIMATION_DURATION = 200L

class GlossaryView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : RelativeLayout(context, attrs) {
    private var currentWord: String = ""
    private var wordTerm: TextView
    private var glossary: TextView
    private var definition: TextView
    private val animationInterpolator = DecelerateInterpolator()
    private var previousHeight = 0
    private val hyperlinkDrawable = R.drawable.ic_external_link_24

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

    fun setWithAnimation(
        word: String,
        glossaryTitle: String,
        wordDefinition: String,
        url: String? = null,
    ) {
        if (currentWord == word) return
        if (word.isBlank()) {
            isVisible = false
            set(word, glossaryTitle, wordDefinition, url)
        } else {
            isVisible = true
            hideDefinition {
                set(word, glossaryTitle, wordDefinition, url)
                showDefinition()
            }
        }
    }

    fun set(word: String, glossaryTitle: String, wordDefinition: String, url: String? = null) {
        if (currentWord == word) return
        currentWord = word
        wordTerm.text = url?.let { makeSpannable(it, word) } ?: currentWord
        wordTerm.movementMethod = LinkMovementMethod.getInstance()
        wordTerm.rightDrawable(if (url != null) hyperlinkDrawable else 0)
        glossary.text = glossaryTitle
        definition.text = wordDefinition
    }

    private fun makeSpannable(url: String, word: String): Spanned {
        val html = "<a href=\"$url\">$word</a>"
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
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

fun TextView.rightDrawable(@DrawableRes drawable: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}

fun View.visible(animate: Boolean = true) {
    if (animate) {
        animate().alpha(1f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                visibility = View.VISIBLE
            }
        })
    } else {
        visibility = View.VISIBLE
    }
}

/** Set the View visibility to INVISIBLE and eventually animate view alpha till 0% */
fun View.invisible(animate: Boolean = true) {
    hide(View.INVISIBLE, animate)
}

/** Set the View visibility to GONE and eventually animate view alpha till 0% */
fun View.gone(animate: Boolean = true) {
    hide(View.GONE, animate)
}

/** Convenient method that chooses between View.visible() or View.invisible() methods */
fun View.visibleOrInvisible(show: Boolean, animate: Boolean = true) {
    if (show) visible(animate) else invisible(animate)
}

/** Convenient method that chooses between View.visible() or View.gone() methods */
fun View.visibleOrGone(show: Boolean, animate: Boolean = true) {
    if (show) visible(animate) else gone(animate)
}

private fun View.hide(hidingStrategy: Int, animate: Boolean = true) {
    if (animate) {
        animate().alpha(0f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = hidingStrategy
            }
        })
    } else {
        visibility = hidingStrategy
    }
}