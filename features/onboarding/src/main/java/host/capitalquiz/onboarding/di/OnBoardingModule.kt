package host.capitalquiz.onboarding.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.onboarding.data.OnBoardingRepository
import host.capitalquiz.onboarding.ui.SettingsRepository


@Module
@InstallIn(ViewModelComponent::class)
interface OnBoardingModule {

    @Binds
    fun bindOnBoardingRepository(impl: OnBoardingRepository): SettingsRepository

}