package host.capitalquiz.editgamerule.ui.editscreen

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.core.ui.collect
import host.capitalquiz.editgamerule.R
import host.capitalquiz.editgamerule.ui.BaseGameRuleFragment
import javax.inject.Inject
import host.capitalquiz.editgamerule.databinding.FragmentEditGameRuleBinding as EditRuleBinding

@AndroidEntryPoint
class EditGameRuleFragment : BaseGameRuleFragment<EditRuleBinding>() {
    override val viewInflater: Inflater<EditRuleBinding> = EditRuleBinding::inflate
    private val args by navArgs<EditGameRuleFragmentArgs>()
    override val fab get() = binding.addLetterFab
    override val recyclerView get() = binding.lettersList

    @Inject
    lateinit var editRuleVMFactory: EditGameRuleViewModelFactory

    private val viewModel: EditGameRuleViewModel by viewModels {
        EditGameRuleViewModel.factory(editRuleVMFactory, args.gameRuleId)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewModel.init(getString(R.string.copy_rule_prefix_name))

        if (args.gameRuleId < 0) {
            viewModel.createNewRule(getString(R.string.deafult_new_rule_name))
        }

        val adapter = RuleLetterAdapter()
        binding.lettersList.adapter = adapter

        viewModel.gameRule.collect(viewLifecycleOwner) {
            binding.rulesToolbar.title = it.name
            adapter.submitList(it.letters)
        }

        binding.addLetterFab.setOnClickListener {
            viewModel.saveLetter(('А'..'Я').random(), (1..15).random())
        }
    }
}