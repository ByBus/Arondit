package host.capitalquiz.editgamerule.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.core.ui.BindingFragment
import host.capitalquiz.core.ui.Inflater
import javax.inject.Inject
import host.capitalquiz.editgamerule.databinding.FragmentGameRulesBinding as GameRulesBinding

@AndroidEntryPoint
class GameRulesFragment : BindingFragment<GameRulesBinding>() {
    override val viewInflater: Inflater<GameRulesBinding> = GameRulesBinding::inflate

    private val args by navArgs<GameRulesFragmentArgs>()

    @Inject
    lateinit var gameRulesViewModelFactory: GameRuleViewModelFactory

    private val viewModel: GameRulesViewModel by viewModels {
        GameRulesViewModel.provideFactory(gameRulesViewModelFactory, args.gameId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GameRulesAdapter(object : GameRulesAdapter.RuleClickListener {
            override fun onRuleClick(ruleId: Long) {
               Toast.makeText(requireContext(), "Rule with id $ruleId", Toast.LENGTH_SHORT).show()
            }

            override fun onDeleteClick(ruleId: Long) {
                TODO("Not yet implemented")
            }

            override fun onEditClick(ruleId: Long) {
                TODO("Not yet implemented")
            }
        })

        binding.rulesList.adapter = adapter

        viewModel.gameRules.observe(viewLifecycleOwner) {
            Log.d("GameRulesFragment", "onViewCreated: $it")
            adapter.submitList(it)
        }

        binding.rulesList.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            binding.addRuleFab.isVisible = scrollY < 0
        }
    }
}