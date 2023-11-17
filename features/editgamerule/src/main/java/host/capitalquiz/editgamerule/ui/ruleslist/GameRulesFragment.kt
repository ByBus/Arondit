package host.capitalquiz.editgamerule.ui.ruleslist

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.editgamerule.ui.BaseGameRuleFragment
import host.capitalquiz.editgamerule.ui.GameRulesNavigation
import javax.inject.Inject
import host.capitalquiz.editgamerule.databinding.FragmentGameRulesBinding as GameRulesBinding

@AndroidEntryPoint
class GameRulesFragment : BaseGameRuleFragment<GameRulesBinding>() {
    override val viewInflater: Inflater<GameRulesBinding> = GameRulesBinding::inflate
    private val args by navArgs<GameRulesFragmentArgs>()
    override val fab get() = binding.createRuleFab
    override val recyclerView get() = binding.rulesList

    @Inject
    lateinit var navigation: GameRulesNavigation

    @Inject
    lateinit var gameRulesViewModelFactory: GameRuleViewModelFactory

    private val viewModel: GameRulesViewModel by viewModels {
        GameRulesViewModel.provideFactory(gameRulesViewModelFactory, args.gameId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        super.onViewCreated(view, savedInstanceState)

        val adapter = GameRulesAdapter(object : GameRulesAdapter.RuleClickListener {
            override fun onRuleClick(ruleId: Long) = viewModel.selectRuleForGame(ruleId)

            override fun onDeleteClick(ruleId: Long) {
                viewModel.deleteGameRule(ruleId)
            }

            override fun onEditClick(ruleId: Long) = navigation.navigateToEditRule(ruleId)
        })

        binding.rulesList.adapter = adapter

        binding.createRuleFab.setOnClickListener {
            navigation.navigateToCreateRule()
        }

        viewModel.gameRules.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}