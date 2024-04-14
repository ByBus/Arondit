package host.capitalquiz.editgamerule.ui.editscreen

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import host.capitalquiz.core.ui.Inflater
import host.capitalquiz.core.ui.collect
import host.capitalquiz.core.ui.getLongOrNull
import host.capitalquiz.editgamerule.R
import host.capitalquiz.editgamerule.ui.BaseGameRuleFragment
import javax.inject.Inject
import host.capitalquiz.editgamerule.databinding.FragmentEditGameRuleBinding as EditRuleBinding

@AndroidEntryPoint
class EditGameRuleFragment : BaseGameRuleFragment<EditRuleBinding>(),
    Toolbar.OnMenuItemClickListener {
    override val viewInflater: Inflater<EditRuleBinding> = EditRuleBinding::inflate
    private val args by navArgs<EditGameRuleFragmentArgs>()
    override val fab get() = binding.addLetterFab
    override val recyclerView get() = binding.lettersList

    @Inject
    lateinit var navigation: EditGameRuleNavigation

    private val viewModel: EditGameRuleViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras
                .withCreationCallback<EditGameRuleViewModelFactory> { factory ->
                    factory.create(args.gameRuleId)
                }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            bundle.getString(NAME_KEY)?.let { viewModel.renameRule(it) }
            bundle.getLongOrNull(RULE_ID_KEY)?.let { viewModel.updateRule(it) }
        }

        if (savedInstanceState == null && args.gameRuleId < 0) {
            viewModel.createNewRule(getString(R.string.deafult_new_rule_name))
        }

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val adapter = RuleLetterAdapter { letter, points ->
            viewModel.goToAddLetterDialog(letter, points)
        }
        binding.lettersList.adapter = adapter

        val toolbar = binding.rulesToolbar
        viewModel.gameRule.collect(viewLifecycleOwner) {
            TransitionManager.beginDelayedTransition(toolbar)
            toolbar.menu.findItem(R.id.edit_rule_name).isVisible = it.readOnly.not()
            toolbar.title = it.name
            adapter.submitList(it.letters)
        }

        viewModel.navigationEvent.collect(viewLifecycleOwner) { navEvent ->
            navEvent.navigate(navigation)
        }

        binding.addLetterFab.setOnClickListener {
            viewModel.goToAddLetterDialog()
        }

        toolbar.apply {
            setOnMenuItemClickListener(this@EditGameRuleFragment)
            setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.edit_rule_name -> {
                viewModel.goToRenameRuleDialog()
                true
            }

            else -> false
        }
    }

    companion object {
        const val REQUEST_KEY = "host.capitalquiz.EditGameRuleFragment.request.key"
        const val NAME_KEY = "host.capitalquiz.EditGameRuleFragment.name.key"
        const val RULE_ID_KEY = "host.capitalquiz.EditGameRuleFragment.ruleId.key"
    }
}