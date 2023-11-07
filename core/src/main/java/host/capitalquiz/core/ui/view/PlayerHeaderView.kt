package host.capitalquiz.core.ui.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import host.capitalquiz.core.R

class PlayerHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : RelativeLayout(context, attrs) {
    private var playerName: ResponsiveTextDrawView
    private var playerScore: HeaderTextView
    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.player_header_layout, this, true)
        playerName = view.findViewById(R.id.fieldPlayerName)
        playerScore = view.findViewById(R.id.fieldPlayerScore)
    }

    fun setScore(value: Int) {
        playerScore.text = value.toString()
    }

    fun setName(value: String) {
        playerName.setText(value)
    }

    fun setColor(@ColorInt value: Int) {
        playerName.setBackgroundColor(value)
        playerScore.setBackgroundColor(value)
        val tintList = ColorStateList.valueOf(value)
        findViewById<ImageView>(R.id.leftHeaderTintPiece).imageTintList = tintList
        findViewById<ImageView>(R.id.rightHeaderTintPiece).imageTintList = tintList
    }

    fun addPlayerButton(): ImageButton = findViewById(R.id.addPlayerButton)

    fun removePlayerButton(): ImageButton = findViewById(R.id.removePlayerButton)

}