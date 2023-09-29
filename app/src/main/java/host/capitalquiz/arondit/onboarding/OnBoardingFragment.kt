package host.capitalquiz.arondit.onboarding

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import host.capitalquiz.arondit.databinding.FragmentOnBoardingBinding as OnBoardingBinding


class OnBoardingFragment : BindingFragment<OnBoardingBinding>() {

    override val viewInflater: Inflater<OnBoardingBinding> = OnBoardingBinding::inflate
    private val viewModel by viewModels<OnBoardingViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

        binding.closeButton.setOnClickListener {

        }

    }

    override fun onResume() {
        super.onResume()
        binding.pager.registerOnPageChangeCallback(callback)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            val pager = binding.pager
            if (pager.currentItem == 0) {
                isEnabled = false
                this.remove()
                parentFragmentManager.popBackStack()
            } else {
                pager.currentItem--
            }
        }
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