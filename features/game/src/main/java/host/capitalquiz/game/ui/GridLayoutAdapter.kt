package host.capitalquiz.game.ui

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
import host.capitalquiz.core.ui.view.PlayerHeaderView
import host.capitalquiz.game.R


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
        private val id: FieldId,
        private val color: FieldColor,
        private var playerName: String,
        private val itemView: View,
        playerId: Long,
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
            header.setOnClickListener {
                listener.onNameClick(playerName, playerId, color)
            }
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

        fun updateField(playerWords: List<WordUi>, playerScore: Int, playerName: String) {
            header.setScore(playerScore)
            header.setName(playerName)
            this.playerName = playerName
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


    private fun createPlayerField(context: Context, field: FieldUi) {
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
        PlayerField(
            FieldId(field.id),
            FieldColor(field.color),
            field.playerName,
            itemView,
            field.playerId
        ).attach()
    }

    fun submitList(context: Context, list: List<FieldUi>) {
        val actualFieldColors = list.associateBy { it.color }
        actualFieldColors.forEach { (fieldColor, field) ->
            if (fields.containsKey(fieldColor).not()) {
                createPlayerField(context, field)
            }
            fields[fieldColor]?.updateField(field.words, field.score, field.playerName)
        }
    }

    fun removeField(color: Int) {
        fields[color]?.detach()
    }

    interface Listener {
        fun onAddPlayerClick()
        fun onRemovePlayerClick(fieldId: FieldId, fieldColor: FieldColor)
        fun onAddWordClick(fieldId: FieldId, fieldColor: FieldColor)
        fun onWordClick(wordId: Long, fieldId: FieldId, fieldColor: FieldColor)
        fun onNameClick(name: String, playerId: Long, fieldColor: FieldColor)
    }
}

@JvmInline
value class FieldId(val value: Long)

@JvmInline
value class FieldColor(val value: Int)