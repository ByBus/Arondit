package host.capitalquiz.editgamerule.ui.editscreen

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.core.ui.BindingFragment
import host.capitalquiz.core.ui.Inflater
import javax.inject.Inject
import host.capitalquiz.editgamerule.databinding.FragmentEditGameRuleBinding as EditRuleBinding

@AndroidEntryPoint
class EditGameRuleFragment : BindingFragment<EditRuleBinding>() {
    override val viewInflater: Inflater<EditRuleBinding> = EditRuleBinding::inflate
    private val args by navArgs<EditGameRuleFragmentArgs>()

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
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val adapter = RuleLetterAdapter()
        binding.lettersList.adapter = adapter

        viewModel.gameRule.observe(viewLifecycleOwner) {
            binding.rulesToolbar.title = it.name
            adapter.submitList(it.letters)
        }
    }
}