package host.capitalquiz.arondit

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import host.capitalquiz.arondit.databinding.FragmentGameBinding


class GameFragment : Fragment() {
    private val viewModel: GameViewModel by viewModels()
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.addColors(
            listOf(
                R.color.player_color_1,
                R.color.player_color_2,
                R.color.player_color_3,
                R.color.player_color_4
            ).map {
                ContextCompat.getColor(requireContext(), it)
            })
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPlayer.setOnClickListener {
            if (binding.grid.childCount == 4) {
                return@setOnClickListener
            }
            val color = viewModel.borrowColor()
            val newPlayerList = LayoutInflater.from(view.context)
                .inflate(R.layout.fragment_game_list, binding.grid, false).apply {
                    id = View.generateViewId()
                }
            val params = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            ).apply {
                height = 0
                width = 0
                bottomMargin = 2
                marginEnd = 2
            }
            newPlayerList.layoutParams = params
            val toolbar = newPlayerList.findViewById<MaterialToolbar>(R.id.toolbar)
            val bottomToolbar = newPlayerList.findViewById<MaterialCardView>(R.id.bottomToolbar)
            toolbar.setBackgroundColor(color)
            bottomToolbar.setBackgroundColor(color)
            toolbar.setOnMenuItemClickListener {
                binding.grid.removeView(newPlayerList)
                viewModel.returnColor(color)
                true
            }
            val recyclerWords = newPlayerList.findViewById<RecyclerView>(R.id.wordsList)
            recyclerWords.layoutManager = LinearLayoutManager(requireContext())
            val adapter = WordAdapter()
            recyclerWords.adapter = adapter
            binding.grid.addView(newPlayerList)
            adapter.submitList(viewModel.words)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}