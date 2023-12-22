package host.capitalquiz.game.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
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
    private var borderDrawable: (() -> Drawable)? = null

    fun bindTo(gridLayout: GridLayout) {
        grid = gridLayout
    }

    fun addDecorationDrawableFactory(decoration: () -> Drawable) {
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
        private var header = itemView.findViewById<PlayerHeaderView>(R.id.playerHeader)
        private var badge = itemView.findViewById<TextView>(R.id.wordsCountBadge)
        private var crown = itemView.findViewById<ImageView>(R.id.crown)

        init {
            recyclerView.adapter = adapter
            header.setColor(color.value)
            header.setName(playerName)
            header.setOnClickListener {
                listener.onNameClick(playerName, playerId, color)
            }
            itemView.findViewById<View>(R.id.bottomToolbar).backgroundTintList =
                ColorStateList.valueOf(color.value)
            header.removePlayerButton().setOnClickListener {
                listener.onRemovePlayerClick(id, color)
            }
            header.addPlayerButton().setOnClickListener {
                listener.onAddPlayerClick()
            }
            itemView.findViewById<Button>(R.id.addWord).setOnClickListener {
                listener.onAddWordClick(id, color)
            }
            borderDrawable?.let {
                itemView.findViewById<View>(R.id.border)?.background = it.invoke()
            }
        }

        fun updateField(
            playerWords: List<WordUi>,
            playerScore: Int,
            playerName: String,
            showCrown: Boolean,
        ) {
            header.setScore(playerScore)
            header.setName(playerName)
            this.playerName = playerName
            updateCrown(showCrown)
            adapter.submitList(playerWords.toMutableList())
            updateWordCountBadge(playerWords.size)
        }

        private fun updateCrown(showCrown: Boolean) {
            if (crown.isVisible != showCrown) {
                startTransition(grid)
                crown.isVisible = showCrown
            }
        }

        private fun updateWordCountBadge(number: Int) {
            val showBadge = number > 0
            badge.isVisible = showBadge
            val text = number.toString()
            if (showBadge && badge.text != text) { // 2nd predicate is needed to avoid updating when deleting/adding a field
                badge.text = text
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

        private fun startTransition(root: ViewGroup) {
            TransitionManager.beginDelayedTransition(root,
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