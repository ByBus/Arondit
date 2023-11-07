package host.capitalquiz.arondit.onboarding.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import host.capitalquiz.core.ui.BindingFragment
import host.capitalquiz.core.ui.CommandScheduler

abstract class BaseOnBoardingFragment<VB : ViewBinding> : BindingFragment<VB>() {
    protected val viewModel by viewModels<OnBoardingViewModel>(ownerProducer = { requireParentFragment() })
    private val scheduler = CommandScheduler()
    protected abstract val positionInViewPager: Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentPage.observe(viewLifecycleOwner) {
            if (it == positionInViewPager) launchAnimation()
        }
    }

    override fun onPause() {
        super.onPause()
        scheduler.cancel()
    }

    private fun launchAnimation() {
        scheduler.schedule {
            animationSchedule()
        }.execute()
    }

    protected abstract fun CommandScheduler.animationSchedule()
}

