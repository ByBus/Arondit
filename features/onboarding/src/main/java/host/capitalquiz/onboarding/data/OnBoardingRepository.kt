package host.capitalquiz.onboarding.data

import host.capitalquiz.core.datastore.SettingsLocalDataSource
import host.capitalquiz.onboarding.ui.SettingsRepository
import javax.inject.Inject

class OnBoardingRepository @Inject constructor(
    private val storage: SettingsLocalDataSource,
) : SettingsRepository {

    override val showOnBoarding = storage.showOnBoardingScreen

    override suspend fun clear() = storage.clear()

    override suspend fun disableOnBoarding() = storage.enableOnBoardingScreen(false)
}