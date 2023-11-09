package host.capitalquiz.onboarding.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val ITEMS_COUNT = 3
class OnBoardingAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
       override fun getItemCount(): Int = ITEMS_COUNT

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HowToAddPlayerFragment()
            1 -> HowToAddWordFragment()
            2 -> HowToChangeWordScoreFragment()
            else -> throw IllegalStateException("Fragment for position $position can't be found")
        }
    }
}