package host.capitalquiz.game.ui

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import host.capitalquiz.core.ui.view.CompositeBorderDrawable
import host.capitalquiz.game.R
import host.capitalquiz.game.databinding.FragmentGameListItemBinding

class GameFieldView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {
    private val binding = FragmentGameListItemBinding.inflate(LayoutInflater.from(context), this)

    init {
        binding.border.background = CompositeBorderDrawable()
    }

    fun updateFieldStats(
        playerName: String,
        playerScore: Int,
        wordsCount: Int,
    ) {
        with(binding) {
            playerHeader.setScore(playerScore)
            playerHeader.setName(playerName)
            updateWordCountBadge(wordsCount)
        }
    }

    fun initPlayer(playerName: String, playerColor: Int) {
        with(binding) {
            playerHeader.setColor(playerColor)
            playerHeader.setName(playerName)
            bottomToolbar.backgroundTintList = ColorStateList.valueOf(playerColor)
        }
    }

    fun crown() = binding.crown

    fun header() = binding.playerHeader

    fun addWordButton() = binding.addWord

    fun wordsList() = binding.wordsList

    private fun updateWordCountBadge(number: Int) {
        val showBadge = number > 0
        val badge = binding.wordsCountBadge
        badge.isVisible = showBadge
        val text = number.toString()
        if (showBadge && badge.text != text) { // 2nd predicate is needed to avoid updating when deleting/adding a field
            badge.text = text
        }
    }

    private fun CompositeBorderDrawable() = CompositeBorderDrawable(
        context,
        leftTopCorner = R.drawable.player_border_top_left_corner,
        leftBottomCorner = R.drawable.player_border_bottom_left_corner,
        leftVerticalPipe = R.drawable.player_border_left_vert_pipe,
        topHorizontalPipe = R.drawable.player_border_top_hor_pipe,
        bottomHorizontalPipe = R.drawable.player_border_top_hor_pipe
    )
}