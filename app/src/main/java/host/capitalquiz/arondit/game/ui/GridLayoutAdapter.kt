package host.capitalquiz.arondit.game.ui

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import host.capitalquiz.arondit.R


class GridLayoutAdapter(
    private val context: Context,
    private val addUserCallBack: () -> Unit,
    private val removeUserCallback: (PlayerId, PlayerColor) -> Unit,
    private val openAddWordDialogCallback: (PlayerId, PlayerColor) -> Unit,
    private val wordClickCallback: (id: Long, PlayerColor, PlayerId) -> Unit,
) {
    private lateinit var grid: GridLayout
    private val fields = mutableMapOf<Int, PlayerField>()

    fun bindTo(gridLayout: GridLayout) {
        grid = gridLayout
    }

    inner class PlayerField(
        val id: PlayerId,
        val color: PlayerColor,
        playerName: String,
        private val itemView: View,
    ) {
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.wordsList)
        private val adapter = WordAdapter { wordId ->
            wordClickCallback.invoke(wordId, color, id)
        }
        private var toolbar: LinearLayout

        init {
            recyclerView.adapter = adapter
            toolbar = itemView.findViewById(R.id.fieldToolbar)
            val bottomToolbar = itemView.findViewById<MaterialCardView>(R.id.bottomToolbar)
            toolbar.setBackgroundColor(color.value)
            toolbar.findViewById<TextView>(R.id.fieldPlayerName).text = playerName
            bottomToolbar.setBackgroundColor(color.value)
            itemView.findViewById<ImageButton>(R.id.removePlayerButton).setOnClickListener {
                removeUserCallback.invoke(id, color)
            }
            itemView.findViewById<ImageButton>(R.id.addPlayerButton).setOnClickListener {
                addUserCallBack.invoke()
            }
            bottomToolbar.findViewById<Button>(R.id.addWord).setOnClickListener {
                openAddWordDialogCallback.invoke(id, color)
            }
        }

        fun updateField(playerWords: List<WordUi>, playerScore: Int) {
            toolbar.findViewById<TextView>(R.id.fieldPlayerScore)?.text = playerScore.toString()
            adapter.submitList(playerWords.toMutableList())
        }

        fun attach() {
            grid.addView(itemView)
            fields[color.value] = this
        }

        fun detach() {
            fields.remove(color.value)
            grid.removeView(itemView)
        }

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createPlayerField(player: PlayerUi) {
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.fragment_game_list, grid, false)
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun submitList(list: List<PlayerUi>) {
        val actualFieldColors = list.associateBy { it.color }
        actualFieldColors.forEach { (fieldColor, player) ->
            if (fields.containsKey(fieldColor).not()) {
                createPlayerField(player)
            }
            fields[fieldColor]?.updateField(player.words, player.score)
        }
    }

    fun removeField(color: Int) {
        fields[color]?.detach()
    }
}

@JvmInline
value class PlayerId(val value: Long)

@JvmInline
value class PlayerColor(val value: Int)