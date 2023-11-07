package host.capitalquiz.arondit.game.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import host.capitalquiz.arondit.R
import host.capitalquiz.core.ui.view.PlayerHeaderView


class GridLayoutAdapter(private val listener: Listener) {

    private lateinit var grid: GridLayout
    private val fields = mutableMapOf<Int, PlayerField>()
    private var borderDrawable: Drawable? = null

    fun bindTo(gridLayout: GridLayout) {
        grid = gridLayout
    }

    fun addDecorationDrawable(decoration: Drawable) {
        borderDrawable = decoration
    }

    inner class PlayerField(
        val id: PlayerId,
        val color: PlayerColor,
        playerName: String,
        private val itemView: View,
    ) {
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.wordsList)
        private val adapter = WordAdapter { wordId ->
            listener.onWordClick(wordId, id, color)
        }
        private var header: PlayerHeaderView

        init {
            recyclerView.adapter = adapter
            header = itemView.findViewById(R.id.playerHeader)
            header.setColor(color.value)
            header.setName(playerName)
            val bottomToolbar = itemView.findViewById<RelativeLayout>(R.id.bottomToolbar)
            bottomToolbar.backgroundTintList = ColorStateList.valueOf(color.value)
            header.removePlayerButton().setOnClickListener {
                listener.onRemovePlayerClick(id, color)
            }
            header.addPlayerButton().setOnClickListener {
                listener.onAddPlayerClick()
            }
            bottomToolbar.findViewById<Button>(R.id.addWord).setOnClickListener {
                listener.onAddWordClick(id, color)
            }
            borderDrawable?.let {
                itemView.findViewById<View>(R.id.border)?.background = it
            }
        }

        fun updateField(playerWords: List<WordUi>, playerScore: Int) {
            header.setScore(playerScore)
            adapter.submitList(playerWords.toMutableList())
        }

        fun attach() {
            startTransition()
            grid.addView(itemView)
            fields[color.value] = this
        }

        fun detach() {
            startTransition()
            fields.remove(color.value)
            grid.removeView(itemView)
        }

        private fun startTransition() {
            TransitionManager.beginDelayedTransition(grid,
                TransitionSet().apply {
                    ordering = TransitionSet.ORDERING_SEQUENTIAL
                    addTransition(ChangeBounds())
                    addTransition(Fade(Fade.IN))
                })
        }

    }


    private fun createPlayerField(context: Context, player: PlayerUi) {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_game_list_item, grid, false)
        itemView.layoutParams = GridLayout.LayoutParams(
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
        ).apply {
            height = 0
            width = 0
            bottomMargin = 2
            marginEnd = 2
        }
        PlayerField(PlayerId(player.id), PlayerColor(player.color), player.name, itemView).attach()
    }

    fun submitList(context: Context, list: List<PlayerUi>) {
        val actualFieldColors = list.associateBy { it.color }
        actualFieldColors.forEach { (fieldColor, player) ->
            if (fields.containsKey(fieldColor).not()) {
                createPlayerField(context, player)
            }
            fields[fieldColor]?.updateField(player.words, player.score)
        }
    }

    fun removeField(color: Int) {
        fields[color]?.detach()
    }

    interface Listener {
        fun onAddPlayerClick()
        fun onRemovePlayerClick(playerId: PlayerId, playerColor: PlayerColor)
        fun onAddWordClick(playerId: PlayerId, playerColor: PlayerColor)
        fun onWordClick(wordId: Long, playerId: PlayerId, playerColor: PlayerColor)
    }
}

@JvmInline
value class PlayerId(val value: Long)

@JvmInline
value class PlayerColor(val value: Int)