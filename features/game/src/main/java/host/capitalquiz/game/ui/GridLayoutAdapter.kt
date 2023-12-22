package host.capitalquiz.game.ui

import android.content.Context
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.view.isVisible
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet


class GridLayoutAdapter(private val listener: Listener) {

    private lateinit var grid: GridLayout
    private val fields = mutableMapOf<Int, PlayerField>()

    fun bindTo(gridLayout: GridLayout) {
        grid = gridLayout
    }

    inner class PlayerField(
        private val id: FieldId,
        private val color: FieldColor,
        private var playerName: String,
        private val itemView: GameFieldView,
        playerId: Long,
    ) {
        private val adapter = WordAdapter { wordId ->
            listener.onWordClick(wordId, id, color)
        }

        init {
            itemView.wordsList().adapter = adapter
            itemView.initPlayer(playerName, color.value)
            val header = itemView.header()
            header.setOnClickListener {
                listener.onNameClick(playerName, playerId, color)
            }
            header.removePlayerButton().setOnClickListener {
                listener.onRemovePlayerClick(id, color)
            }
            header.addPlayerButton().setOnClickListener {
                listener.onAddPlayerClick()
            }
            itemView.addWordButton().setOnClickListener {
                listener.onAddWordClick(id, color)
            }
        }

        fun updateField(
            playerWords: List<WordUi>,
            playerScore: Int,
            playerName: String,
            showCrown: Boolean,
        ) {
            itemView.updateFieldStats(playerName, playerScore, playerWords.size)
            this.playerName = playerName
            updateCrown(showCrown)
            adapter.submitList(playerWords.toList())
        }

        private fun updateCrown(showCrown: Boolean) {
            val crown = itemView.crown()
            if (crown.isVisible != showCrown) {
                startTransition(grid)
                crown.isVisible = showCrown
            }
        }

        fun attach() {
            startTransition(grid)
            grid.addView(itemView)
            fields[color.value] = this
        }

        fun detach() {
            startTransition(grid)
            fields.remove(color.value)
            grid.removeView(itemView)
        }

    }

    private fun createPlayerField(context: Context, field: FieldUi) {
        val itemView = GameFieldView(context)
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
            fields[fieldColor]?.updateField(
                field.words,
                field.score,
                field.playerName,
                field.showCrown
            )
        }
    }

    fun removeField(color: Int) {
        fields[color]?.detach()
    }

    private fun startTransition(root: ViewGroup) {
        TransitionManager.beginDelayedTransition(root,
            TransitionSet().apply {
                ordering = TransitionSet.ORDERING_SEQUENTIAL
                addTransition(ChangeBounds())
                addTransition(Fade(Fade.IN))
            })
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