package host.capitalquiz.arondit.onboarding.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import host.capitalquiz.arondit.onboarding.data.OnBoardingRepository
import host.capitalquiz.arondit.core.datastore.SettingsLocalDataSource
import host.capitalquiz.arondit.onboarding.ui.SettingsRepository


@Module
@InstallIn(ViewModelComponent::class)
abstract class OnBoardingModule {

    @Binds
    abstract fun bindOnBoardingRepository(impl: OnBoardingRepository): SettingsRepository


    @Binds
    abstract fun bindSettingsLocalDataSource(impl: SettingsLocalDataSource.BaseDataStore): SettingsLocalDataSource
}