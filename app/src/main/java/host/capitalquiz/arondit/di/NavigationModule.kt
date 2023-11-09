package host.capitalquiz.arondit.di

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import host.capitalquiz.arondit.navigation.GameFragmentNavigation
import host.capitalquiz.arondit.navigation.GamesListFragmentNavigation
import host.capitalquiz.arondit.navigation.OnBoardingFragmentNavigation
import host.capitalquiz.game.ui.GameNavigation
import host.capitalquiz.gameslist.ui.GamesListNavigation
import host.capitalquiz.onboarding.ui.OnBoardingNavigation

@Module
@InstallIn(FragmentComponent::class)
interface NavigationModule {

    @Binds
    fun bindGamesListNavigation(impl: GamesListFragmentNavigation): GamesListNavigation

    @Binds
    fun bindGameNavigation(impl: GameFragmentNavigation): GameNavigation

    @Binds
    fun bindOnBoardingNavigation(impl: OnBoardingFragmentNavigation): OnBoardingNavigation

    companion object {

        @Provides
        fun provideNavController(fragment: Fragment): NavController = fragment.findNavController()

    }
}
