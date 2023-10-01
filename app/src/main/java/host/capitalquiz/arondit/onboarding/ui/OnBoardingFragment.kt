package host.capitalquiz.arondit.onboarding.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import host.capitalquiz.arondit.core.ui.BindingFragment
import host.capitalquiz.arondit.core.ui.Inflater
import host.capitalquiz.arondit.databinding.FragmentOnBoardingBinding as OnBoardingBinding


@AndroidEntryPoint
class OnBoardingFragment : BindingFragment<OnBoardingBinding>() {
    private val args by navArgs<OnBoardingFragmentArgs>()
    override val viewInflater: Inflater<OnBoardingBinding> = OnBoardingBinding::inflate
    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        super.onViewCreated(view, savedInstanceState)

        val fragmentsAdapter = OnBoardingAdapter(this)
        val pager = binding.pager.apply {
            offscreenPageLimit = 1
            adapter = fragmentsAdapter
        }
        binding.pagination.attachTo(pager, viewLifecycleOwner)

        binding.nextButton.setOnClickListener {
            pager.currentItem++
        }

        viewModel.currentPage.observe(viewLifecycleOwner) {
            val nextButtonVisibility = it < fragmentsAdapter.itemCount - 1
            binding.nextButton.isVisible = nextButtonVisibility
            binding.closeButton.isVisible = nextButtonVisibility.not()
        }

        viewModel.showOnBoarding.observe(viewLifecycleOwner) { showOnboarding ->
            if (showOnboarding.not()) {
                val direction = OnBoardingFragmentDirections.actionToGameFragment(args.gameId)
                findNavController().navigate(direction)
            }
        }

        binding.closeButton.setOnClickListener {
            viewModel.closeOnBoarding()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val pager = binding.pager
            if (pager.currentItem == 0) {
                isEnabled = false
                this.remove()
                findNavController().popBackStack()
            } else {
                pager.currentItem--
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.pager.registerOnPageChangeCallback(callback)
    }

    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onStop() {
        super.onStop()
        binding.pager.unregisterOnPageChangeCallback(callback)
    }

    private val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                viewModel.updateCurrent(binding.pager.currentItem)
            }
        }
    }
}